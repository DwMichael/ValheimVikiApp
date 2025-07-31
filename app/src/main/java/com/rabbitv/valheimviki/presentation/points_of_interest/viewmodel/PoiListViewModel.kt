package com.rabbitv.valheimviki.presentation.points_of_interest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.presentation.points_of_interest.model.PoiListUiEvent
import com.rabbitv.valheimviki.presentation.points_of_interest.model.PoiListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class PoiListViewModel @Inject constructor(
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _selectedSubCategory = MutableStateFlow(PointOfInterestSubCategory.FORSAKEN_ALTAR)
	internal val filteredPoiListWithCategory: Flow<Pair<List<PointOfInterest>, PointOfInterestSubCategory>> =
		pointOfInterestUseCases.getLocalPointOfInterestUseCase().combine(
			_selectedSubCategory,
		) { all, category ->
			val list = all.filter { it.subCategory == category.toString() }
				.sortedBy { it.order }
			Pair(list, category)
		}.flowOn(defaultDispatcher)


	val uiState: StateFlow<PoiListUiState> = combine(
		filteredPoiListWithCategory,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = true
		),
	) { (poiList, subCategory), isConnected ->
		when {
			poiList.isNotEmpty() -> {
				PoiListUiState(
					selectedSubCategory = subCategory,
					poiListState = UIState.Success(poiList)
				)
			}

			isConnected -> PoiListUiState(
				selectedSubCategory = subCategory,
				poiListState = UIState.Loading
			)

			else -> PoiListUiState(
				selectedSubCategory = subCategory,
				poiListState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}.catch { e ->
		Log.e("PoiListVM", "Error in uiState flow", e)
		emit(
			PoiListUiState(
				selectedSubCategory = _selectedSubCategory.value,
				poiListState = UIState.Error(e.message ?: "An unknown error occurred")
			)
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = PoiListUiState(
			selectedSubCategory = PointOfInterestSubCategory.FORSAKEN_ALTAR,
			poiListState = UIState.Loading
		)
	)

	fun onEvent(event: PoiListUiEvent) {
		when (event) {
			is PoiListUiEvent.CategorySelected -> {
				_selectedSubCategory.update { event.category }
			}
		}
	}
}