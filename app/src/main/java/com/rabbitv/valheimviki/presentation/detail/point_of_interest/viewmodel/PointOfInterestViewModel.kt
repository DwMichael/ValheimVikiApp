package com.rabbitv.valheimviki.presentation.detail.point_of_interest.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.model.PointOfInterestUiState
import com.rabbitv.valheimviki.utils.Constants.POINT_OF_INTEREST_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class PointOfInterestViewModel @Inject constructor(
	private val _pointOfInterestUseCases: PointOfInterestUseCases,
	private val _biomeUseCases: BiomeUseCases,
	private val _creatureUseCases: CreatureUseCases,
	private val _materialUseCases: MaterialUseCases,
	private val _relationUseCases: RelationUseCases,
	val savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val _pointOfInterestId: String = checkNotNull(savedStateHandle[POINT_OF_INTEREST_KEY])
	private val _pointOfInterest = MutableStateFlow<PointOfInterest?>(null)
	private val _relatedBiomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _relatedCreatures = MutableStateFlow<List<Creature>>(emptyList())
	private val _relatedOfferings = MutableStateFlow<List<Material>>(emptyList())
	private val _relatedMaterialDrops = MutableStateFlow<List<MaterialDrop>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)


	val uiState: StateFlow<PointOfInterestUiState> = combine(
		_pointOfInterest,
		_relatedBiomes,
		_relatedCreatures,
		_relatedOfferings,
		_relatedMaterialDrops,
		_isLoading,
		_error
	) { values ->
		PointOfInterestUiState(
			pointOfInterest = values[0] as PointOfInterest?,
			relatedBiomes = values[1] as List<Biome>,
			relatedCreatures = values[2] as List<Creature>,
			relatedOfferings = values[3] as List<Material>,
			relatedMaterialDrops = values[4] as List<MaterialDrop>,
			isLoading = values[5] as Boolean,
			error = values[6] as String?
		)

	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = PointOfInterestUiState()
	)

	init {
		loadPointOfInterestData()
	}

	internal fun loadPointOfInterestData() {
		try {
			viewModelScope.launch(Dispatchers.IO) {
				_isLoading.value = true
				_pointOfInterest.value =
					_pointOfInterestUseCases.getPointOfInterestByIdUseCase(_pointOfInterestId)
						.first()

				val relatedObjects: List<RelatedItem> = async {
					_relationUseCases.getRelatedIdsUseCase(_pointOfInterestId)
						.first()
				}.await()

				val relatedIds = relatedObjects.map { it.id }

				launch {
					_relatedBiomes.value = _biomeUseCases.getBiomesByIdsUseCase(relatedIds).first()
				}
				launch {
					_relatedCreatures.value =
						_creatureUseCases.getCreaturesByIds(relatedIds).first()
				}

				launch {
					val allRematedMaterials =
						_materialUseCases.getMaterialsByIds(relatedIds).first()

					val tempList = mutableListOf<MaterialDrop>()
					_relatedOfferings.value =
						allRematedMaterials.filter { it.subCategory == MaterialSubCategory.FORSAKEN_ALTAR_OFFERING.toString() }
					val restOfMaterials =
						allRematedMaterials.filter { it.subCategory != MaterialSubCategory.FORSAKEN_ALTAR_OFFERING.toString() }
					val relatedItemsMap = relatedObjects.associateBy { it.id }
					for (material in restOfMaterials) {
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

					_relatedMaterialDrops.value = tempList


				}
			}
			_isLoading.value = false
		} catch (e: Exception) {
			Log.e("General fetch error PointOfInterestDetailViewModel", e.message.toString())
			_isLoading.value = false
			_error.value = e.message
		}
	}
}