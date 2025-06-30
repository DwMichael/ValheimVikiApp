package com.rabbitv.valheimviki.presentation.detail.material.mob_drop.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreatures
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.material.mob_drop.model.MobDropUiState
import com.rabbitv.valheimviki.utils.Constants.MOB_DROP_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MobDropDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val relationUseCases: RelationUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	private val _materialId: String = checkNotNull(savedStateHandle[MOB_DROP_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _passiveCreatures = MutableStateFlow<List<PassiveCreature>>(emptyList())
	private val _aggressiveCreature = MutableStateFlow<List<AggressiveCreature>>(emptyList())
	private val _pointsOfInterest = MutableStateFlow<List<PointOfInterest>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState = combine(
		_material,
		_passiveCreatures,
		_aggressiveCreature,
		_pointsOfInterest,
		_isLoading,
		_error
	) { values ->
		MobDropUiState(
			material = values[0] as Material?,
			passive = values[1] as List<PassiveCreature>,
			aggressive = values[2] as List<AggressiveCreature>,
			pointsOfInterest = values[3] as List<PointOfInterest>,
			isLoading = values[4] as Boolean,
			error = values[5] as String?
		)

	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		MobDropUiState()
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


				_passiveCreatures.value = passiveDeferred.await()
				_aggressiveCreature.value = aggressiveDeferred.await()
				_pointsOfInterest.value = pointOfInterestDeferred.await()

			} catch (e: Exception) {
				Log.e("MobDropDetailViewModel", "General fetch error: ${e.message}", e)
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}

		}


	}
}