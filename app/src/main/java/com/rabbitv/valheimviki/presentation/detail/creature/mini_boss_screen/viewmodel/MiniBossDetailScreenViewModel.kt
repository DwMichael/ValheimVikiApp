package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.creatures.toMiniBoss
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.navigation.CreatureDetailDestination
import com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.model.MiniBossDetailUIEvent
import com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.model.MiniBossDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MiniBossDetailScreenViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val creatureUseCases: CreatureUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val materialUseCases: MaterialUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _miniBossId: String =
		savedStateHandle.toRoute<CreatureDetailDestination.MiniBossDetail>().miniBossId

	private val _miniBoss: StateFlow<MiniBoss?> =
		creatureUseCases.getCreatureById(_miniBossId).map { creature ->
			creature?.toMiniBoss()
		}.stateIn(
			viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = null
		)

	private val _isFavorite = favoriteUseCases.isFavorite(_miniBossId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)

	private val _relationObjects =
		relationUseCases.getRelatedIdsUseCase(_miniBossId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)

	private val idsAndMap: Flow<List<String>> =
		_relationObjects
			.map { list -> list.map { it.id } }
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)

	private val _primarySpawn = idsAndMap.flatMapLatest { ids ->
		pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(ids)
	}.map { pointsOfInterest ->
		pointsOfInterest.firstOrNull()
	}.stateIn(
		viewModelScope,
		started = SharingStarted.Lazily,
		initialValue = null
	)

	private val _dropItems = idsAndMap.flatMapLatest { ids ->
		materialUseCases.getMaterialsBySubCategoryAndIds(
			MaterialSubCategory.MINI_BOSS_DROP.toString(),
			ids
		)
	}.map { UIState.Success(it) }.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		UIState.Loading
	)

	val uiState = combine(
		_miniBoss,
		_primarySpawn,
		_dropItems,
		_isFavorite
	) { miniBoss, primarySpawn, dropItems, favorite ->
		MiniBossDetailUiState(
			miniBoss = miniBoss,
			primarySpawn = primarySpawn,
			dropItems = dropItems,
			isFavorite = favorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = MiniBossDetailUiState()
	)

	fun uiEvent(event: MiniBossDetailUIEvent) {
		when (event) {
			MiniBossDetailUIEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_miniBoss.value?.let { bM ->
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