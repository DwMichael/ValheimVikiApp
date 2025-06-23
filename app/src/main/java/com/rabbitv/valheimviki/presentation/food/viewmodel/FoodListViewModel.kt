package com.rabbitv.valheimviki.presentation.food.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val foodUseCases: FoodUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {

    private val _selectedSubCategory =
        MutableStateFlow<FoodSubCategory>(FoodSubCategory.COOKED_FOOD)

    private val _foods: StateFlow<List<Food>> = _selectedSubCategory.flatMapLatest { subCategory ->
        foodUseCases.getFoodBySubCategoryUseCase(subCategory).catch { e ->
            emit(emptyList<Food>())
        }
    }.flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<UiCategoryState<FoodSubCategory, Food>> = combine(
        _foods,
        _selectedSubCategory,
        connectivityObserver.isConnected.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        ),
    ) { food, subCategory, isConnected ->
        if (isConnected) {
            if (food.isNotEmpty()) {
                UiCategoryState.Success(subCategory, food)
            } else {
                UiCategoryState.Loading(subCategory)
            }
        } else {
            if (food.isNotEmpty()) {
                UiCategoryState.Success(subCategory, food)
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
        Log.e("FoodListVM", "Error in uiState flow", e)
        emit(
            UiCategoryState.Error(
                _selectedSubCategory.value,
                e.message ?: "An unknown error occurred"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiCategoryState.Loading(_selectedSubCategory.value)
    )

    fun onCategorySelected(cat: FoodSubCategory) {
        _selectedSubCategory.value = cat
    }
}