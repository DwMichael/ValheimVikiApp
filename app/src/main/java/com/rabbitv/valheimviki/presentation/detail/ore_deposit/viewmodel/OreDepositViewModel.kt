package com.rabbitv.valheimviki.presentation.detail.ore_deposit.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.model.OreDepositUiState
import com.rabbitv.valheimviki.utils.Constants.ORE_DEPOSIT_KEY
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

@HiltViewModel
class OreDepositViewModel @Inject constructor(
	private val oreDepositUseCases: OreDepositUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	val savedStateHandle: SavedStateHandle
) : ViewModel() {
	private val _oreDepositId: String = checkNotNull(savedStateHandle[ORE_DEPOSIT_KEY])
	private val _oreDeposit = MutableStateFlow<OreDeposit?>(null)
	private val _biomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _materials = MutableStateFlow<List<MaterialDrop>>(emptyList())

	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState: StateFlow<OreDepositUiState> = combine(
		_oreDeposit,
		_biomes,
		_materials,
		_isLoading,
		_error
	) { oreDeposit, biomes, materials, isLoading, error ->
		OreDepositUiState(
			oreDeposit = oreDeposit,
			relatedBiomes = biomes,
			relatedMaterials = materials,
			isLoading = isLoading,
			error = error
		)

	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = OreDepositUiState()
	)

	init {
		loadOreDepositDetail()
	}


	internal fun loadOreDepositDetail() {
		try {

			viewModelScope.launch(Dispatchers.IO) {
				_isLoading.value = true
				launch {
					_oreDeposit.value =
						oreDepositUseCases.getOreDepositByIdUseCase(_oreDepositId).first()
				}


				val relationObjects = async {
					relationUseCases.getRelatedIdsUseCase(_oreDepositId).first()
				}.await()

				val relationIds = relationObjects.map { it.id }
				launch {
					_biomes.value = biomeUseCases.getBiomesByIdsUseCase(relationIds).first()
				}
				launch {
					val materials =
						materialUseCases.getMaterialsByIds(relationIds).first()

					val tempList = mutableListOf<MaterialDrop>()
					val relatedItemsMap = relationObjects.associateBy { it.id }
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
					_materials.value = tempList
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