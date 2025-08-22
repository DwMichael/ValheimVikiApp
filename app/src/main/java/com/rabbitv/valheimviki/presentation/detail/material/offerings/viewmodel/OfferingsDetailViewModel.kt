package com.rabbitv.valheimviki.presentation.detail.material.offerings.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreatures
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.material.mob_drop.model.MobDropUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.offerings.model.OfferingUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.offerings.model.OfferingUiState
import com.rabbitv.valheimviki.utils.Constants.OFFERINGS_MATERIAL_KEY
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
class OfferingsDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	private val _materialId: String = checkNotNull(savedStateHandle[OFFERINGS_MATERIAL_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _passiveCreatures = MutableStateFlow<List<PassiveCreature>>(emptyList())
	private val _aggressiveCreature = MutableStateFlow<List<AggressiveCreature>>(emptyList())
	private val _pointsOfInterest = MutableStateFlow<List<PointOfInterest>>(emptyList())
	private val _altars = MutableStateFlow<List<PointOfInterest>>(emptyList())
	private val _craftingStation = MutableStateFlow<List<CraftingObject>>(emptyList())
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
		_passiveCreatures,
		_aggressiveCreature,
		_pointsOfInterest,
		_altars,
		_craftingStation,
		_isFavorite,
		_isLoading,
		_error
	) { material, passiveC,aggressiveC,poiOfInterest, altars, craftingStation, favorite, isLoading, error ->
		OfferingUiState(
			material = material,
			passive =  passiveC,
			aggressive = aggressiveC,
			pointsOfInterest = poiOfInterest,
			altars = altars,
			craftingStation = craftingStation,
			isFavorite = favorite,
			isLoading = isLoading,
			error = error
		)

	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		OfferingUiState()
	)

	init {
		loadMobDropData()
	}

	internal fun loadMobDropData() {

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

				val relationIds = relationObjects.map { it.id }


				val passiveDeferred = async {
					val creatures = creatureUseCases.getCreaturesByIds(
						relationIds
					).first()
					creatures.filter { it.subCategory == CreatureSubCategory.PASSIVE_CREATURE.toString() }
						.toPassiveCreatures()
				}

				val aggressiveDeferred = async {
					val creatures = creatureUseCases.getCreaturesByIds(
						relationIds
					).first()
					creatures.filter { it.subCategory == CreatureSubCategory.AGGRESSIVE_CREATURE.toString() }
						.toAggressiveCreatures()
				}

				val pointOfInterestDeferred = async {
					pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(
						relationIds
					).first()

				}

				val craftingStationDeferred = async {
					craftingObjectUseCases.getCraftingObjectsByIds(
						relationIds
					).first()

				}


				_passiveCreatures.value = passiveDeferred.await()
				_aggressiveCreature.value = aggressiveDeferred.await()
				_pointsOfInterest.value = pointOfInterestDeferred.await()
					.filter { it.subCategory == PointOfInterestSubCategory.STRUCTURE.toString() }
				_altars.value = pointOfInterestDeferred.await()
					.filter { it.subCategory == PointOfInterestSubCategory.FORSAKEN_ALTAR.toString() }
				_craftingStation.value = craftingStationDeferred.await()

			} catch (e: Exception) {
				Log.e("MobDropDetailViewModel", "General fetch error: ${e.message}", e)
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun uiEvent(event: OfferingUiEvent) {
		when (event) {
			OfferingUiEvent.ToggleFavorite -> {
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