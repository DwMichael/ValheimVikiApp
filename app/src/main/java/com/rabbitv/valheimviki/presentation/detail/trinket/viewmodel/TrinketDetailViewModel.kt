package com.rabbitv.valheimviki.presentation.detail.trinket.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.trinket.TrinketUseCases
import com.rabbitv.valheimviki.navigation.EquipmentDetailDestination
import com.rabbitv.valheimviki.presentation.detail.armor.model.ArmorDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.armor.model.ArmorDetailUiState
import com.rabbitv.valheimviki.presentation.detail.trinket.model.TrinketDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.trinket.model.TrinketDetailUiState
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TrinketDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val trinketUseCases: TrinketUseCases,
	private val relationsUseCases: RelationUseCases,
	private val materialUseCases: MaterialUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _trinketId: String =
		savedStateHandle.toRoute<EquipmentDetailDestination.TrinketDetail>().trinketId
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_trinketId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _trinketFlow: StateFlow<Trinket?> =
		trinketUseCases.getTrinketByIdUseCase(_trinketId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _relationsObjects: StateFlow<List<RelatedItem>> =
		relationsUseCases.getRelatedIdsUseCase(_trinketId)
			.distinctUntilChanged()
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

	private val _relatedMaterials: StateFlow<UIState<List<MaterialUpgrade>>> =
		_relationsObjects.filter { it.isNotEmpty() }.flatMapLatest { relatedObjects ->
			val relatedMap = relatedObjects.associateBy { it.id }
			val ids = relatedObjects.map { it.id }

			materialUseCases.getMaterialsByIds(ids)
				.map { materials: List<Material> ->
					materials.map { material ->
						val rel = relatedMap[material.id]
						MaterialUpgrade(
							material = material,
							quantityList = listOf(
								rel?.quantity,
								rel?.quantity2star,
								rel?.quantity3star,
								rel?.quantity4star
							)
						)
					}
				}
		}.map<List<MaterialUpgrade>, UIState<List<MaterialUpgrade>>> { UIState.Success(it) }
			.flowOn(defaultDispatcher)
			.catch { e -> emit(UIState.Error(e.message ?: "Error fetching related materials")) }
			.stateIn(
				viewModelScope,
				SharingStarted.WhileSubscribed(5_000),
				UIState.Loading
			)


	private val _relatedCraftingObject = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> craftingObjectUseCases.getCraftingObjectByIds(ids) },
	).flowOn(defaultDispatcher)

	val uiState: StateFlow<TrinketDetailUiState> = combine(
		_trinketFlow,
		_relatedMaterials,
		_relatedCraftingObject,
		_isFavorite,
	) { trinket, materials, craftingObjects, favorite ->
		TrinketDetailUiState(
			trinket = trinket,
			materials = materials,
			craftingObject = craftingObjects,
			isFavorite = favorite,
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = TrinketDetailUiState()
	)

	fun uiEvent(event: TrinketDetailUiEvent) {
		when (event) {
			is TrinketDetailUiEvent.ToggleFavorite ->
				viewModelScope.launch {
					val target = !_isFavorite.value
					_isFavorite.value = target
					favoriteUseCases.toggleFavoriteUseCase(event.favorite, target)
				}

		}
	}
}
