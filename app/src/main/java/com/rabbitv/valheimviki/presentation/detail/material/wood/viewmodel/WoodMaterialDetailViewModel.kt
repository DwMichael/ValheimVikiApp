package com.rabbitv.valheimviki.presentation.detail.material.wood.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.presentation.detail.material.valuable.model.ValuableUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.wood.model.WoodUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.wood.model.WoodUiState
import com.rabbitv.valheimviki.utils.Constants.WOOD_MATERIAL_KEY
import com.rabbitv.valheimviki.utils.extensions.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
@HiltViewModel
class WoodMaterialDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val treeUseCases: TreeUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	private val _materialId: String = checkNotNull(savedStateHandle[WOOD_MATERIAL_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _biomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _trees = MutableStateFlow<List<Tree>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)
	private val _isFavorite = favoriteUseCases.isFavorite(_materialId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)

	val uiState = combine(
		_material,
		_biomes,
		_trees,
		_isFavorite,
		_isLoading,
		_error
	) { material, biomes,trees,favorite, isLoading, error ->
		WoodUiState(
			material =material,
			biomes = biomes,
			trees = trees,
			isFavorite = favorite,
			isLoading = isLoading,
			error = error
		)

	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		WoodUiState()
	)

	init {
		loadMobDropData()
	}

	internal fun loadMobDropData() {

		viewModelScope.launch(Dispatchers.IO) {
			try {
				_isLoading.value = true
				_error.value = null


				val materialDeferred = async {
					materialUseCases.getMaterialById(_materialId).first()
				}


				val relationObjectsDeferred = async {
					relationUseCases.getRelatedIdsUseCase(_materialId).first()
				}


				val material = materialDeferred.await()
				val relationObjects = relationObjectsDeferred.await()

				_material.value = material

				val relationIds = relationObjects.map { it.id }

				val treesDeferred = async {
					treeUseCases.getTreesByIdsUseCase(
						relationIds
					).first()
				}

				val biomesDeferred = async {
					biomeUseCases.getBiomesByIdsUseCase(
						relationIds
					).first()
				}
				_biomes.value = biomesDeferred.await()
				_trees.value = treesDeferred.await()

			} catch (e: Exception) {
				Log.e("WoodMaterialDetailViewModel", "General fetch error: ${e.message}", e)
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun uiEvent(event: WoodUiEvent) {
		when (event) {
			WoodUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_material.value?.let { bM ->
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