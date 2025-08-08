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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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

	private val _treeFlow: Flow<Tree?> = treeUseCases.getTreeByIdUseCase(_treeId)
	private val _relatedBiomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _relatedMaterials = MutableStateFlow<List<MaterialDrop>>(emptyList())
	private val _relatedAxes = MutableStateFlow<List<Weapon>>(emptyList())
	private val _isLoading = MutableStateFlow(true) // Initialize as true
	private val _error = MutableStateFlow<String?>(null)

	val treeUiState: StateFlow<TreeDetailUiState> = combine(
		_treeFlow,
		_relatedBiomes,
		_relatedMaterials,
		_relatedAxes,
		favoriteUseCases.isFavorite(_treeId),
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
		loadAllTreeData()
	}


	internal fun loadAllTreeData() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.value = true
			try {

				val relatedObjects: List<RelatedItem> =
					relationsUseCase.getRelatedIdsUseCase(_treeId).first()
				val relatedIds = relatedObjects.map { it.id }

				val biomesJob = async {
					biomeUseCases.getBiomesByIdsUseCase(relatedIds).first().sortedBy { it.order }
				}

				val materialsJob = async {
					val materials = materialUseCases.getMaterialsByIds(relatedIds).first()
					val tempList = mutableListOf<MaterialDrop>()
					val relatedItemsMap = relatedObjects.associateBy { it.id }
					materials.forEach { material ->
						val relatedItem = relatedItemsMap[material.id]
						tempList.add(
							MaterialDrop(
								itemDrop = material,
								quantityList = listOf(relatedItem?.quantity),
								chanceStarList = listOf(relatedItem?.chance1star)
							)
						)
					}
					tempList
				}

				val axesJob = async {
					weaponUseCase.getWeaponsByIdsUseCase(relatedIds).first().sortedBy { it.order }
				}


				_relatedBiomes.value = biomesJob.await()
				_relatedMaterials.value = materialsJob.await()
				_relatedAxes.value = axesJob.await()


				_isLoading.value = false
			} catch (e: Exception) {
				Log.e("TreeDetailViewModel", "Error fetching tree details: ${e.message}", e)
				_error.value = "Failed to load tree details: ${e.localizedMessage}"
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