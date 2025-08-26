package com.rabbitv.valheimviki.presentation.detail.material.crafted.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.relation.RelationType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.MaterialDetailDestination
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.crafted.model.CraftedMaterialUiState
import com.rabbitv.valheimviki.presentation.detail.material.model.MaterialToCraft
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CraftedMaterialDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val materialUseCases: MaterialUseCases,
	private val craftingUseCases: CraftingObjectUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _materialId: String =
		savedStateHandle.toRoute<MaterialDetailDestination.CraftedMaterialDetail>().craftedMaterialId
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

	private val _requiredCraftingStation = relatedDataFlow(
		idsFlow = _relationsObjects
			.map { list ->
				list.filter { it.relationType == RelationType.PRODUCES.toString() }
					.map { item -> item.id }
			},
		fetcher = { ids -> craftingUseCases.getCraftingObjectsByIds(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedMaterials: StateFlow<UIState<List<MaterialToCraft>>> =
		_relationsObjects
			.filter { it.isNotEmpty() }
			.flatMapLatest { relatedObjects ->
				val relationToBuildIds = relatedObjects
					.filter { it.relationType == RelationType.TO_BUILD.toString() }
					.map { it.id }

				if (relationToBuildIds.isEmpty()) {
					kotlinx.coroutines.flow.flowOf(emptyList<MaterialToCraft>())
				} else {
					val relatedItemsMap = relatedObjects.associateBy { it.id }
					materialUseCases.getMaterialsByIds(relationToBuildIds)
						.map { materials ->
							materials.map { material ->
								val relatedItem = relatedItemsMap[material.id]
								val quantityList = listOf<Int?>(
									relatedItem?.quantity,
									relatedItem?.quantity2star,
									relatedItem?.quantity3star
								)
								MaterialToCraft(
									material = material,
									quantityList = quantityList
								)
							}
						}
				}
			}
			.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)
			.stateIn(
				viewModelScope,
				SharingStarted.WhileSubscribed(5_000),
				UIState.Loading
			)

	val uiState: StateFlow<CraftedMaterialUiState> = combine(
		_materialFlow,
		_requiredCraftingStation,
		_relatedMaterials,
		_isFavorite
	) { material, requiredCraftingStation, relatedMaterials, isFavorite ->
		CraftedMaterialUiState(
			material = material,
			requiredCraftingStations = requiredCraftingStation,
			relatedMaterial = relatedMaterials,
			isFavorite = isFavorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = CraftedMaterialUiState()
	)

	fun uiEvent(event: CraftingDetailUiEvent) {
		when (event) {
			CraftingDetailUiEvent.ToggleFavorite -> {
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