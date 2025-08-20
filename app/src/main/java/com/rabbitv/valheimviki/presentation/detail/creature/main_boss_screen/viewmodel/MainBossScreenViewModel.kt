package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBoss
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.model.relation.RelatedData
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.CreatureDetailDestination
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model.MainBossDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model.MainBossUiEvent
import com.rabbitv.valheimviki.utils.extensions.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainBossScreenViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val creatureUseCases: CreatureUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val materialUseCases: MaterialUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private data class MaterialBossData(
		val dropItems: List<Material> =emptyList(),
		val trophy: Material? = null
	)
	private val _mainBossId: String =savedStateHandle.toRoute<CreatureDetailDestination.MainBossDetail>().mainBossId


	private val _mainBoss: StateFlow<MainBoss?> = creatureUseCases.getCreatureById(_mainBossId).map { creature ->
	creature?.toMainBoss()
	}.stateIn(
			viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = null
		)

	private val _isFavorite = favoriteUseCases.isFavorite(_mainBossId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)
	private val _relationObjects =
		relationUseCases.getRelatedIdsUseCase(_mainBossId).stateIn(
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
		biomes.find { it.id in relatedData.ids }
	}.stateIn(
		viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = null
	)

	private val _forsakenAltar = idsAndMap.combine(
		pointOfInterestUseCases
			.getPointsOfInterestBySubCategoryUseCase(PointOfInterestSubCategory.FORSAKEN_ALTAR),
	) { relatedData, altar ->
		altar.find { it.id in relatedData.ids }
	}.stateIn(
		viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = null
	)
	private val _sacrificialStones = idsAndMap.combine(
		pointOfInterestUseCases
			.getPointsOfInterestBySubCategoryUseCase(PointOfInterestSubCategory.STRUCTURE),
	) { relatedData, stones ->
		stones.find {  it.imageUrl == "https://static.wikia.nocookie.net/valheim/images/2/29/Sarcrifial_Stones.jpg/revision/latest?cb=20230416093844"}
	}.stateIn(
		viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = null
	)


	private val _relatedSummoningItems = idsAndMap.flatMapLatest{ relatedData ->
		merge(
			materialUseCases.getMaterialsBySubCategoryAndIds(
				subCategory = MaterialSubCategory.FORSAKEN_ALTAR_OFFERING.toString(),
				ids = relatedData.ids
			),
			materialUseCases.getMaterialsBySubCategoryAndIds(
				subCategory = MaterialSubCategory.CREATURE_DROP.toString(),
				ids = relatedData.ids
			),
		).map { materials -> materials.distinctBy { it.id } }
	}.map { UIState.Success(it) } .stateIn(
		viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = UIState.Loading
	)
	private val _materialBossData :StateFlow<MaterialBossData> = idsAndMap.flatMapLatest{ relatedData ->
		materialUseCases.getMaterialsBySubCategoryAndIds(MaterialSubCategory.BOSS_DROP.toString(), relatedData.ids )
	}.map { materials ->
		MaterialBossData(
			dropItems = materials,
			trophy = materials.find {
				it.subType == MaterialSubType.TROPHY.toString()
			}
		)
	}.stateIn(
		viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = MaterialBossData()
	)

	val uiState = combine(
		_mainBoss,
		_biome,
		_forsakenAltar,
		_sacrificialStones,
		_materialBossData,
		_relatedSummoningItems,
		favoriteUseCases.isFavorite(_mainBossId),

	) { mainBoss,biome, forsakenAltar, sacrificialStones,materialBossData, summoningItems, favorite->
		MainBossDetailUiState(
			mainBoss = mainBoss,
			relatedBiome =  biome,
			relatedForsakenAltar = forsakenAltar,
			sacrificialStones = sacrificialStones,
			dropItems = UIState.Success( materialBossData.dropItems),
			relatedSummoningItems =summoningItems,
			trophy = materialBossData.trophy,
			isFavorite = favorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = MainBossDetailUiState()
	)

	fun uiEvent(event: MainBossUiEvent) {
		when (event) {
			MainBossUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_mainBoss.value?.let { bM ->
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