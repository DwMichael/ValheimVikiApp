package com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreature
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.CreatureDetailDestination
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model.PassiveCreatureDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model.PassiveCreatureDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PassiveCreatureDetailScreenViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val materialUseCases: MaterialUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _passiveCreatureId: String = savedStateHandle.toRoute<CreatureDetailDestination.PassiveCreatureDetail>().passiveCreatureId
	private val _creature = creatureUseCases.getCreatureById(_passiveCreatureId)
		.map { creature -> creature?.toPassiveCreature() }
		.stateIn(
			viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = null
		)
	private val _isFavorite = favoriteUseCases.isFavorite(_passiveCreatureId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)
	private val _relationObjects =
		relationUseCases.getRelatedIdsUseCase(_passiveCreatureId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)
	private val idsAndMap: Flow<RelatedData> =
		_relationObjects
			.map { list ->
				val relatedMap = list.associateBy(RelatedItem::id)
				val ids = relatedMap.keys.sorted()
				RelatedData(
					ids = ids,
					relatedMap = relatedMap
				)
			}
			.filter { it.ids.isNotEmpty() }
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)

	private val _biome = idsAndMap.combine(
		biomeUseCases.getLocalBiomesUseCase(),
	) { relatedData, biomes ->
		biomes.find { it.id in relatedData.ids}
	}
	private val _materialDropItem =
		idsAndMap.flatMapLatest { (ids, currentItemsMap) ->
			materialUseCases.getMaterialsByIds(ids)
				.map { list ->
					list.filter { it.subCategory != MaterialSubCategory.FORSAKEN_ALTAR_OFFERING.toString() }
						.map { material ->
						val relatedItem = currentItemsMap[material.id]
						MaterialDrop(
							itemDrop = material,
							quantityList = listOf(
								relatedItem?.quantity,
								relatedItem?.quantity2star,
								relatedItem?.quantity3star
							),
							chanceStarList = listOf(
								relatedItem?.chance1star,
								relatedItem?.chance2star,
								relatedItem?.chance3star
							),
						)
					}
				}
		}.distinctUntilChanged()
			.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)

	val uiState = combine(
		_creature,
		_biome,
		_materialDropItem,
		_isFavorite,
	) { creature, biome, materialDropItems, favorite ->
		PassiveCreatureDetailUiState(
			passiveCreature = creature,
			biome = biome,
			materialDrops = materialDropItems,
			isFavorite = favorite,
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = PassiveCreatureDetailUiState()
	)


	fun uiEvent(event: PassiveCreatureDetailUiEvent) {
		when (event) {
			PassiveCreatureDetailUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_creature.value?.let { bM ->
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