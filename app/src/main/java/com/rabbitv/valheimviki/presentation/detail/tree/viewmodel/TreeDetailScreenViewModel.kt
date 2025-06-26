@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.tree.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
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
	private val weaponUseCase: WeaponUseCases
) : ViewModel() {
	private val _treeId: String = checkNotNull(savedStateHandle[TREE_KEY])
	private val _tree = MutableStateFlow<Tree?>(null)
	private val _relatedBiomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _relatedMaterials = MutableStateFlow<List<Material>>(emptyList())
	private val _isLoading = MutableStateFlow(true)
	private val _error = MutableStateFlow<String?>(null)

	val treeUiState: StateFlow<TreeDetailUiState> = combine(
		_tree,
		_relatedBiomes,
		_relatedMaterials,
		_isLoading,
		_error
	) { tree, biomes, materials, isLoading, error ->
		TreeDetailUiState(
			tree = tree,
			relatedBiomes = biomes,
			relatedMaterials = materials,
			isLoading = isLoading,
			error = error
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = TreeDetailUiState()
	)


	init {
		initialtreeData()
	}

	internal fun initialtreeData() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.value = true
			try {
				_tree.value = treeUseCases.getTreeByIdUseCase(_treeId).firstOrNull()

				val relatedIds: List<String> = async {
					relationsUseCase.getRelatedIdsUseCase(_treeId)
						.first()
						.map { it.id }
				}.await()



				launch {
					_relatedBiomes.value =
						biomeUseCases.getBiomesByIdsUseCase(relatedIds).first()
				}

				launch {
					try {
						_relatedOreDeposits.value =
							oreDepositUseCases.getOreDepositsByIdsUseCase(relatedIds).first()

					} catch (e: Exception) {
						Log.e("Ore deposits fetch error treeDetailViewModel", e.message.toString())
					}
				}
				launch {
					try {
						_relatedMaterials.value =
							materialUseCases.getMaterialsByIds(relatedIds).first()
					} catch (e: Exception) {
						Log.e("Materials fetch error treeDetailViewModel", e.message.toString())
					}
				}


				launch {
					try {
						_relatedPointOfInterest.value =
							pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(relatedIds)
								.first()
					} catch (e: Exception) {
						Log.e(
							"PointOfInterest fetch error treeDetailViewModel",
							e.message.toString()
						)
					}
				}
				launch {
					try {
						_relatedTrees.value = treeUseCases.getTreesByIdsUseCase(relatedIds).first()
					} catch (e: Exception) {
						Log.e("Trees fetch error treeDetailViewModel", e.message.toString())
					}
				}
				_isLoading.value = false
			} catch (e: Exception) {
				Log.e("General fetch error treeDetailViewModel", e.message.toString())
				_isLoading.value = false
			}
		}
	}
}