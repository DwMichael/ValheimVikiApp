package com.rabbitv.valheimviki.presentation.points_of_interest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PoiListViewModel @Inject constructor(
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
	private val _selectedSubCategory =
		MutableStateFlow<PointOfInterestSubCategory>(PointOfInterestSubCategory.FORSAKEN_ALTAR)

	private val _poiList: StateFlow<List<PointOfInterest>> =
		combine(
			pointOfInterestUseCases.getLocalPointOfInterestUseCase(),
			_selectedSubCategory,
		) { all, category ->
			all.filter { it.subCategory == category.toString() }
				.sortedBy { it.order }
		}.flowOn(Dispatchers.Default)
			.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

	val uiState: StateFlow<UiCategoryState<PointOfInterestSubCategory, PointOfInterest>> = combine(
		_poiList,
		_selectedSubCategory,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = false
		),
	) { poiList, subCategory, isConnected ->
		if (isConnected) {
			if (poiList.isNotEmpty()) {
				UiCategoryState.Success(subCategory, poiList)
			} else {
				UiCategoryState.Loading(subCategory)
			}
		} else {
			if (poiList.isNotEmpty()) {
				UiCategoryState.Success(subCategory, poiList)
			} else {
				UiCategoryState.Error(
					subCategory,
					"No internet connection and no local data available. Try to connect to the internet again.",
				)
			}
		}

	}.onStart {
		emit(UiCategoryState.Loading(_selectedSubCategory.value))
	}.catch { e ->
		Log.e("PointOfInterestListVM", "Error in uiState flow", e)
		emit(
			UiCategoryState.Error(
				_selectedSubCategory.value,
				e.message ?: "An unknown error occurred"
			)
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Companion.WhileSubscribed(5000),
		UiCategoryState.Loading(_selectedSubCategory.value)
	)

	fun onCategorySelected(cat: PointOfInterestSubCategory) {
		_selectedSubCategory.value = cat
	}
}