package com.rabbitv.valheimviki.presentation.detail.material.seeds.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.navigation.MaterialDetailDestination
import com.rabbitv.valheimviki.presentation.detail.material.seeds.model.SeedUiEvent
import com.rabbitv.valheimviki.presentation.detail.material.seeds.model.SeedUiState
import com.rabbitv.valheimviki.utils.extensions.combine
import com.rabbitv.valheimviki.utils.relatedDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
class SeedMaterialDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val materialUseCases: MaterialUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val treeUseCases: TreeUseCases,
	private val toolUseCases: ToolUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _materialId: String =
		savedStateHandle.toRoute<MaterialDetailDestination.SeedDetail>().seedMaterialId
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

	private val _relatedBiomes = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> biomeUseCases.getBiomesByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedPointsOfInterest = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids ->
			pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids)
				.map { points ->
					points.filter { it.subCategory == PointOfInterestSubCategory.STRUCTURE.toString() }
				}
		}
	).flowOn(defaultDispatcher)

	private val _relatedNpc = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids ->
			creatureUseCases.getCreatureByRelationAndSubCategory(ids, CreatureSubCategory.NPC)
				.map { creature -> creature?.toNPC() }
		}
	).flowOn(defaultDispatcher)

	private val _relatedTools = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> toolUseCases.getToolsByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	private val _relatedTrees = relatedDataFlow(
		idsFlow = _relationsObjects.map { list -> list.map { item -> item.id } },
		fetcher = { ids -> treeUseCases.getTreesByIdsUseCase(ids) }
	).flowOn(defaultDispatcher)

	val uiState: StateFlow<SeedUiState> = combine(
		_materialFlow,
		_relatedBiomes,
		_relatedPointsOfInterest,
		_relatedNpc,
		_relatedTools,
		_relatedTrees,
		_isFavorite
	) { material, biomes, pointsOfInterest, npc, tools, trees, isFavorite ->
		SeedUiState(
			material = material,
			biomes = biomes,
			pointsOfInterest = pointsOfInterest,
			npc = npc,
			tools = tools,
			trees = trees,
			isFavorite = isFavorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = SeedUiState()
	)

	fun uiEvent(event: SeedUiEvent) {
		when (event) {
			SeedUiEvent.ToggleFavorite -> {
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