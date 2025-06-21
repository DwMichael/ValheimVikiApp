package com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.creature.components.MaterialDrop
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model.PassiveCreatureDetailUiState
import com.rabbitv.valheimviki.utils.Constants.PASSIVE_CREATURE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PassiveCreatureDetailScreenViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val materialUseCases: MaterialUseCases,
) : ViewModel() {
	private val _passiveCreatureId: String =
		checkNotNull(savedStateHandle[PASSIVE_CREATURE_KEY])
	private val _creature = MutableStateFlow<PassiveCreature?>(null)
	private val _biome = MutableStateFlow<Biome?>(null)
	private val _Material_dropItems = MutableStateFlow<List<MaterialDrop>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)


	val uiState = combine(
		_creature,
		_biome,
		_Material_dropItems,
		_isLoading,
		_error,
	) { values ->
		@Suppress("UNCHECKED_CAST")
		(PassiveCreatureDetailUiState(
			passiveCreature = values[0] as PassiveCreature?,
			biome = values[1] as Biome?,
			materialDrops = values[2] as List<MaterialDrop>,
			isLoading = values[3] as Boolean,
			error = values[4] as String?
		))
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = PassiveCreatureDetailUiState()
	)

	init {
		launch()
	}


	internal fun launch() {
		try {
			_isLoading.value = true
			viewModelScope.launch(Dispatchers.IO) {

				_creature.value = CreatureFactory.createFromCreature(
					creatureUseCases.getCreatureById(_passiveCreatureId).first()
				)

				val relatedObjects: List<RelatedItem> = async {
					relationUseCases.getRelatedIdsUseCase(_passiveCreatureId)
						.first()

				}.await()
				val relatedIds = relatedObjects.map { it.id }

				val deferreds = listOf(
					async {
						val biome = biomeUseCases.getLocalBiomesUseCase().first()
						_biome.value = biome.find {
							it.id in relatedIds
						}
					},
					async {
						try {
							val materials = materialUseCases.getMaterialsByIds(relatedIds).first()
							val tempList = mutableListOf<MaterialDrop>()

							val relatedItemsMap = relatedObjects.associateBy { it.id }
							for (material in materials) {
								val relatedItem = relatedItemsMap[material.id]
								val quantityList = listOf<Int?>(
									relatedItem?.quantity,
									relatedItem?.quantity2star,
									relatedItem?.quantity3star
								)
								val chanceStarList = listOf<Int?>(
									relatedItem?.chance1star,
									relatedItem?.chance2star,
									relatedItem?.chance3star
								)
								tempList.add(
									MaterialDrop(
										material = material,
										quantityList = quantityList,
										chanceStarList = chanceStarList,
									)
								)
							}
							_Material_dropItems.value = tempList
							Log.e("DROP ITEMS ", "$tempList")
						} catch (e: Exception) {
							Log.e("PassiveCreatureDetail ViewModel", "$e")
							_Material_dropItems.value = emptyList()
						}
					},
				)
				deferreds.awaitAll()
			}
			_isLoading.value = false
		} catch (e: Exception) {
			Log.e("General fetch error PassiveDetailViewModel", e.message.toString())
			_isLoading.value = false
			_error.value = e.message
		}
	}
}