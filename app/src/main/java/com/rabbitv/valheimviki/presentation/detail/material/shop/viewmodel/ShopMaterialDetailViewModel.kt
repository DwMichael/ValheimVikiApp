package com.rabbitv.valheimviki.presentation.detail.material.shop.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.material.shop.model.ShopUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.shop.model.ShopUiState
import com.rabbitv.valheimviki.utils.Constants.SHOP_MATERIAL_KEY

import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopMaterialDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val materialUseCases: MaterialUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	
	private val _materialId: String = checkNotNull(savedStateHandle[SHOP_MATERIAL_KEY])
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_materialId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _materialFlow: StateFlow<Material?> =
		materialUseCases.getMaterialById(_materialId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _relationsObjects: StateFlow<List<RelatedItem>> =
		relationUseCases.getRelatedIdsUseCase(_materialId)
			.distinctUntilChanged()
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

	private val _relatedNpc = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> 
			creatureUseCases.getCreatureByRelationAndSubCategory(ids, CreatureSubCategory.NPC)
				.map { creature -> creature?.toNPC() }
		}
	).flowOn(defaultDispatcher)

	val uiState: StateFlow<ShopUiState> = combine(
		_materialFlow,
		_relatedNpc,
		_isFavorite
	) { material, npc, isFavorite ->
		ShopUiState(
			material = material,
			npc = npc,
			isFavorite = isFavorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = ShopUiState()
	)

	fun uiEvent(event: ShopUiEvent) {
		when (event) {
			ShopUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_materialFlow.value?.let { material ->
						favoriteUseCases.toggleFavoriteUseCase(
							material.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}