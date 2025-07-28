package com.rabbitv.valheimviki.presentation.food.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.presentation.food.model.FoodListUiEvent
import com.rabbitv.valheimviki.presentation.food.model.FoodListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FoodListViewModel @Inject constructor(
	val foodUseCases: FoodUseCases,
	val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
	private val _selectedSubCategory =
		MutableStateFlow(FoodSubCategory.COOKED_FOOD)
	internal val foods: Flow<List<Food>> = _selectedSubCategory.flatMapLatest { subCategory ->
		foodUseCases.getFoodBySubCategoryUseCase(subCategory).catch { e ->
			emit(emptyList())
		}
	}

	val uiState: StateFlow<FoodListUiState> = combine(
		foods,
		_selectedSubCategory,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = false
		),
	) { food, subCategory, isConnected ->
		when {
			food.isNotEmpty() -> {
				FoodListUiState(
					selectedCategory = subCategory,
					foodState = UIState.Success(food)
				)
			}

			isConnected -> FoodListUiState(
				selectedCategory = subCategory,
				foodState = UIState.Loading
			)

			else -> FoodListUiState(
				selectedCategory = subCategory,
				foodState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}.catch { e ->
		Log.e("FoodListVM", "Error in uiState flow", e)
		emit(
			FoodListUiState(
				selectedCategory = _selectedSubCategory.value,
				foodState = UIState.Error("An unknown error occurred")
			)
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = FoodListUiState(
			selectedCategory = FoodSubCategory.COOKED_FOOD,
			foodState = UIState.Loading
		)
	)

	fun onEvent(event: FoodListUiEvent) {
		when (event) {
			is FoodListUiEvent.CategorySelected -> {
				_selectedSubCategory.update { event.category }
			}
		}
	}
}