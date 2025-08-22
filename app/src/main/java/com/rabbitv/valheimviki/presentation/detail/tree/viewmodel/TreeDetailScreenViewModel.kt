@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.tree.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
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
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.model.PointOfInterestUiEvent
import com.rabbitv.valheimviki.presentation.detail.tree.model.TreeDetailUiState
import com.rabbitv.valheimviki.presentation.detail.tree.model.TreeUiEvent
import com.rabbitv.valheimviki.utils.Constants.TREE_KEY
import com.rabbitv.valheimviki.utils.extensions.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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

	private val _tree: StateFlow<Tree?> = treeUseCases.getTreeByIdUseCase(_treeId).stateIn(
		viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = null
	)
	private val _relatedBiomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _relatedMaterials = MutableStateFlow<List<MaterialDrop>>(emptyList())
	private val _relatedAxes = MutableStateFlow<List<Weapon>>(emptyList())
	private val _isLoading = MutableStateFlow(true) // Initialize as true
	private val _error = MutableStateFlow<String?>(null)
	private val _isFavorite = favoriteUseCases.isFavorite(_treeId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)
	//TODO VALUSES
	val treeUiState: StateFlow<TreeDetailUiState> = combine(
		_tree,
		_relatedBiomes,
		_relatedMaterials,
		_relatedAxes,
		_isFavorite,
		_isLoading,
		_error
	) { tree, relatedBiomes, relatedMaterials, relatedAxes, isFavorite, isLoading, error ->
		TreeDetailUiState(
			tree = tree,
			relatedBiomes = relatedBiomes,
			relatedMaterials = relatedMaterials,
			relatedAxes = relatedAxes,
			isFavorite = isFavorite,
			isLoading = isLoading,
			error = error
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

	fun uiEvent(event: TreeUiEvent) {
		when (event) {
			TreeUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_tree.value?.let { bM ->
						favoriteUseCases.toggleFavoriteUseCase(
							bM.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}