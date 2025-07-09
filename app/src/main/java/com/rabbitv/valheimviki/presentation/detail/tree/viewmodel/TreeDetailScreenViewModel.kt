@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.tree.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.presentation.detail.tree.model.TreeDetailUiState
import com.rabbitv.valheimviki.utils.Constants.TREE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TreeDetailScreenViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val treeUseCases: TreeUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val relationsUseCase: RelationUseCases,
	private val materialUseCases: MaterialUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	private val weaponUseCase: WeaponUseCases
) : ViewModel() {
	private val _treeId: String = checkNotNull(savedStateHandle[TREE_KEY])
	private val _tree = MutableStateFlow<Tree?>(null)
	private val _relatedBiomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _relatedMaterials = MutableStateFlow<List<MaterialDrop>>(emptyList())
	private val _relatedAxes = MutableStateFlow<List<Weapon>>(emptyList())
	private val _isLoading = MutableStateFlow(true)
	private val _error = MutableStateFlow<String?>(null)

	val treeUiState: StateFlow<TreeDetailUiState> = combine(
		_tree,
		_relatedBiomes,
		_relatedMaterials,
		_relatedAxes,
		favoriteUseCases.isFavorite(_treeId)
			.flowOn(Dispatchers.IO),
		_isLoading,
		_error
	) { values ->
		TreeDetailUiState(
			tree = values[0] as Tree,
			relatedBiomes = values[1] as List<Biome>,
			relatedMaterials = values[2] as List<MaterialDrop>,
			relatedAxes = values[3] as List<Weapon>,
			isFavorite = values[4] as Boolean,
			isLoading = values[5] as Boolean,
			error = values[6] as String?
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = TreeDetailUiState()
	)


	init {
		initialTreeData()
	}

	internal fun initialTreeData() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.value = true
			try {
				_tree.value = treeUseCases.getTreeByIdUseCase(_treeId).firstOrNull()

				val relatedObjects: List<RelatedItem> = async {
					relationsUseCase.getRelatedIdsUseCase(_treeId)
						.first()
				}.await()

				val relatedIds = relatedObjects.map { it.id }



				launch {
					_relatedBiomes.value =
						biomeUseCases.getBiomesByIdsUseCase(relatedIds).first()
							.sortedBy { it.order }
				}

				launch {

					val materials = materialUseCases.getMaterialsByIds(relatedIds).first()
					val tempList = mutableListOf<MaterialDrop>()

					val relatedItemsMap = relatedObjects.associateBy { it.id }
					for (material in materials) {
						val relatedItem = relatedItemsMap[material.id]
						val quantityList = listOf<Int?>(
							relatedItem?.quantity,
						)
						val chanceStarList = listOf(
							relatedItem?.chance1star,
						)
						tempList.add(
							MaterialDrop(
								itemDrop = material,
								quantityList = quantityList,
								chanceStarList = chanceStarList
							)
						)
					}

					_relatedMaterials.value = tempList
				}

				launch {
					_relatedAxes.value =
						weaponUseCase.getWeaponsByIdsUseCase(relatedIds).first()
							.sortedBy { it.order }
				}

				_isLoading.value = false
			} catch (e: Exception) {
				Log.e("General fetch error treeDetailViewModel", e.message.toString())
				_isLoading.value = false
			}
		}
	}

	fun toggleFavorite(favorite: Favorite, currentIsFavorite: Boolean) {
		viewModelScope.launch {
			if (currentIsFavorite) {
				favoriteUseCases.deleteFavoriteUseCase(favorite)
			} else {
				favoriteUseCases.addFavoriteUseCase(favorite)
			}
		}
	}
}