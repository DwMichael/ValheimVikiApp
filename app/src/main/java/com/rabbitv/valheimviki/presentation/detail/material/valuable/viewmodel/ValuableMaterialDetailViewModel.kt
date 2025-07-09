package com.rabbitv.valheimviki.presentation.detail.material.valuable.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.material.valuable.model.ValuableMaterialUiState
import com.rabbitv.valheimviki.utils.Constants.VALUABLE_MATERIAL_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
@HiltViewModel
class ValuableMaterialDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	private val _materialId: String = checkNotNull(savedStateHandle[VALUABLE_MATERIAL_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _pointsOfInterest = MutableStateFlow<List<PointOfInterest>>(emptyList())
	private val _npc = MutableStateFlow<List<NPC>>(emptyList())
	private val _creatures = MutableStateFlow<List<Creature>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState = combine(
		_material,
		_pointsOfInterest,
		_npc,
		_creatures,
		favoriteUseCases.isFavorite(_materialId)
			.flowOn(Dispatchers.IO),
		_isLoading,
		_error
	) { values ->
		ValuableMaterialUiState(
			material = values[0] as Material?,
			pointsOfInterest = values[1] as List<PointOfInterest>,
			npc = values[2] as List<NPC>,
			creatures = values[3] as List<Creature>,
			isFavorite = values[4] as Boolean,
			isLoading = values[5] as Boolean,
			error = values[6] as String?,
		)

	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		ValuableMaterialUiState()
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

				val pointOfInterestDeferred = async {
					pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(
						relationIds
					).first()
				}

				val creaturesDeferred = async {
					creatureUseCases.getCreaturesByIds(relationIds).first()
				}

				_pointsOfInterest.value = pointOfInterestDeferred.await()
					.filter { it.subCategory == PointOfInterestSubCategory.STRUCTURE.toString() }
				_npc.value = creaturesDeferred.await()
					.filter { it.subCategory == CreatureSubCategory.NPC.toString() }.toNPC()
				_creatures.value = creaturesDeferred.await()
					.filter { it.subCategory != CreatureSubCategory.NPC.toString() }


			} catch (e: Exception) {
				Log.e("SeedMaterialDetailViewModel", "General fetch error: ${e.message}", e)
				_error.value = e.message
			} finally {
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