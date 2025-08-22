package com.rabbitv.valheimviki.presentation.detail.material.general.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.RelationType
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.presentation.detail.material.gemstones.model.GemstoneUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.general.model.GeneralMaterialUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.general.model.GeneralMaterialUiState
import com.rabbitv.valheimviki.utils.Constants.GENERAL_MATERIAL_KEY
import com.rabbitv.valheimviki.utils.extensions.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
@HiltViewModel
class GeneralMaterialDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val craftingUseCases: CraftingObjectUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val oreDepositUseCases: OreDepositUseCases,
	private val treeUseCases: TreeUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val _materialId: String = checkNotNull(savedStateHandle[GENERAL_MATERIAL_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _requiredCraftingStation = MutableStateFlow<List<CraftingObject>>(emptyList())
	private val _biomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _oreDeposits = MutableStateFlow<List<OreDeposit>>(emptyList())
	private val _pointOfInterests = MutableStateFlow<List<PointOfInterest>>(emptyList())
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
		_requiredCraftingStation,
		_pointOfInterests,
		_oreDeposits,
		_trees,
		_isFavorite,
		_isLoading,
		_error
	) { material,biomes,requiredCraftingStation, poitOfInterest, oreDeposits,trees,favorite,isLoading,error ->
		GeneralMaterialUiState(
			material = material,
			biomes = biomes,
			craftingStations = requiredCraftingStation,
			pointOfInterests = poitOfInterest,
			oreDeposits = oreDeposits,
			trees = trees,
			isFavorite = favorite,
			isLoading = isLoading,
			error = error,
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		GeneralMaterialUiState()
	)

	init {
		loadGeneralDropData()
	}

	internal fun loadGeneralDropData() {

		viewModelScope.launch {
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

				val relationsIds = relationObjects.map { it.id }
				val relationProducesIds =
					relationObjects.filter { it.relationType == RelationType.PRODUCES.toString() }
						.map { it.id }


				val biomesDeferred = async {
					biomeUseCases.getBiomesByIdsUseCase(relationsIds).first()
				}

				val oreDepositsDeferred = async {
					oreDepositUseCases.getOreDepositsByIdsUseCase(relationsIds).first()
				}

				val requiredCraftingStationDeferred = async {
					craftingUseCases.getCraftingObjectsByIds(relationProducesIds).first()
				}

				val pointOfInterestsDeferred = async {
					pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(relationsIds).first()
				}

				val treesDeferred = async {
					treeUseCases.getTreesByIdsUseCase(relationsIds).first()
				}


				_biomes.value = biomesDeferred.await()
				_oreDeposits.value = oreDepositsDeferred.await()
				_pointOfInterests.value = pointOfInterestsDeferred.await()
				_trees.value = treesDeferred.await()
				_requiredCraftingStation.value = requiredCraftingStationDeferred.await()

			} catch (e: Exception) {
				Log.e("GeneralMaterialDetailViewModel", "General fetch error: ${e.message}", e)
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}

		}
	}

	fun uiEvent(event: GeneralMaterialUiEvent) {
		when (event) {
			GeneralMaterialUiEvent.ToggleFavorite -> {
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