@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.tree.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.navigation.WorldDetailDestination
import com.rabbitv.valheimviki.presentation.detail.tree.model.TreeDetailUiState
import com.rabbitv.valheimviki.presentation.detail.tree.model.TreeUiEvent
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TreeDetailScreenViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val treeUseCases: TreeUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val relationsUseCase: RelationUseCases,
	private val materialUseCases: MaterialUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	private val weaponUseCase: WeaponUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _treeId: String =
		savedStateHandle.toRoute<WorldDetailDestination.TreeDetail>().treeId
	private val _isFavorite = MutableStateFlow(false)

	init {
		favoriteUseCases.isFavorite(_treeId)
			.distinctUntilChanged()
			.onEach { value -> _isFavorite.value = value }
			.launchIn(viewModelScope)
	}

	private val _treeFlow: StateFlow<Tree?> = treeUseCases.getTreeByIdUseCase(_treeId).stateIn(
		viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = null
	)

	private val _relationsObjects =
		relationsUseCase.getRelatedIdsUseCase(_treeId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)
	private val idsAndMap: Flow<RelatedData> =
		_relationsObjects
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
	private val _relatedBiomes = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> biomeUseCases.getBiomesByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedMaterials =
		idsAndMap.flatMapLatest { (ids, relatedItems) ->
			materialUseCases.getMaterialsByIds(ids)
				.map { list ->
					list.map { material ->
						val relatedItem = relatedItems[material.id]
						MaterialDrop(
							itemDrop = material,
							quantityList = listOf(relatedItem?.quantity),
							chanceStarList = listOf(relatedItem?.chance1star)
						)
					}
				}
		}.distinctUntilChanged()
			.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)


	private val _relatedAxes = relatedDataFlow(
		idsFlow = _relationsObjects.mapLatest { list -> list.map { item -> item.id } },
		fetcher = { ids -> weaponUseCase.getWeaponsByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	val treeUiState: StateFlow<TreeDetailUiState> = combine(
		_treeFlow,
		_relatedBiomes,
		_relatedMaterials,
		_relatedAxes,
		_isFavorite
	) { tree, relatedBiomes, relatedMaterials, relatedAxes, isFavorite ->
		TreeDetailUiState(
			tree = tree,
			relatedBiomes = relatedBiomes,
			relatedMaterials = relatedMaterials,
			relatedAxes = relatedAxes,
			isFavorite = isFavorite,
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = TreeDetailUiState()
	)

	fun uiEvent(event: TreeUiEvent) {
		when (event) {
			TreeUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_treeFlow.value?.let { tree ->
						favoriteUseCases.toggleFavoriteUseCase(
							tree.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}
	}
}