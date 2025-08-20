package com.rabbitv.valheimviki.presentation.detail.creature.npc.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreature
import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.CreatureDetailDestination
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model.MainBossUiEvent
import com.rabbitv.valheimviki.presentation.detail.creature.npc.model.NpcDetailUIEvent
import com.rabbitv.valheimviki.presentation.detail.creature.npc.model.NpcDetailUiState
import com.rabbitv.valheimviki.utils.extensions.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NpcDetailScreenViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val materialUseCases: MaterialUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _npcId: String =savedStateHandle.toRoute<CreatureDetailDestination.NpcDetail>().npcId

	private val _creature = creatureUseCases.getCreatureById(_npcId)
		.map { creature -> creature?.toNPC() }
		.stateIn(
			viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = null
		)
	private val _isFavorite = favoriteUseCases.isFavorite(_npcId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)
	private val _relationObjects =
		relationUseCases.getRelatedIdsUseCase(_npcId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)
	private val idsAndMap: Flow<List<String>> =
		_relationObjects
			.map { list -> list.map { it.id } }
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)
	private val _biome = idsAndMap.combine(
		biomeUseCases.getLocalBiomesUseCase(),
	) { ids, biomes ->
		biomes.find { it.id in ids}
	}.stateIn(
		viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = null
	)
	private val _shopItems =
		idsAndMap.flatMapLatest { ids ->
			materialUseCases.getMaterialsBySubCategoryAndIds(MaterialSubCategory.SHOP.toString(), ids)
		}.distinctUntilChanged()
			.map { UIState.Success(it.sortedBy { it.order }) }
			.flowOn(defaultDispatcher)
	private val _shopSellItems =
		idsAndMap.flatMapLatest { ids ->
			materialUseCases.getMaterialsBySubCategoryAndIds(MaterialSubCategory.VALUABLE.toString(), ids)
		}.distinctUntilChanged()
			.map { UIState.Success(it.sortedBy { it.order }) }
			.flowOn(defaultDispatcher)
	private val _hildirChests =
		idsAndMap.flatMapLatest { ids ->
			materialUseCases.getMaterialsBySubCategoryAndIds(MaterialSubCategory.MINI_BOSS_DROP.toString(), ids)
		}.distinctUntilChanged()
			.map { UIState.Success(it.sortedBy { it.order }) }
			.flowOn(defaultDispatcher)


	private val _chestsLocation =
		idsAndMap.flatMapLatest { ids ->
			pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids)
		}.distinctUntilChanged()
			.map { UIState.Success(it.sortedBy { it.order }) }
			.flowOn(defaultDispatcher)



	val uiState = combine(
		_creature,
		_biome,
		_shopItems,
		_shopSellItems,
		_hildirChests,
		_chestsLocation,
		_isFavorite
	) { npc, biome, shopItems, shopSellItems,hidirChests, chestsLocation,favorite ->
		NpcDetailUiState(
			npc = npc,
			biome = biome,
			shopItems = shopItems,
			shopSellItems = shopSellItems,
			hildirChests = hidirChests,
			chestsLocation = chestsLocation,
			isFavorite = favorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = NpcDetailUiState()
	)


	fun uiEvent(event: NpcDetailUIEvent) {
		when (event) {
			NpcDetailUIEvent.ToggleFavorite -> {
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