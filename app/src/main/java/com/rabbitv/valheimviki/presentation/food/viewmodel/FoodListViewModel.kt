package com.rabbitv.valheimviki.presentation.food.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.FoodFetchException
import com.rabbitv.valheimviki.domain.exceptions.FoodFetchLocalException
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.presentation.food.model.FoodListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val foodUseCases: FoodUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    private val _foodList = MutableStateFlow<List<Food>>(emptyList())
    private val _selectedSubCategory =
        MutableStateFlow<FoodSubCategory>(FoodSubCategory.COOKED_FOOD)


    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _foodList,
        _selectedSubCategory,
        _isConnection,
        _isLoading,
        _error
    ) { values ->
        FoodListUiState(
            foodList = values[0] as List<Food>,
            selectedSubCategory = values[1] as FoodSubCategory,
            isConnection = values[2] as Boolean,
            isLoading = values[3] as Boolean,
            error = values[4] as String?,
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        FoodListUiState()
    )


    init {
        launch()
    }

    internal fun launch(subCategory: FoodSubCategory = FoodSubCategory.COOKED_FOOD) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                foodUseCases.getFoodBySubCategoryUseCase(subCategory)
                    .collect { food ->
                        _foodList.value = food
                    }
                _isLoading.value = false
                _error.value = null
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "somthing goes wrong"
                when (e) {
                    is FoodFetchException -> Log.e(
                        "FoodFetchException FoodListScreenViewModel",
                        "${e.message}"
                    )

                    is FoodFetchLocalException -> Log.e(
                        "FoodFetchLocalException FoodListScreenViewModel",
                        "${e.message}"
                    )

                    else -> Log.e(
                        "Unexpected Exception occurred FoodListScreenViewModel",
                        "${e.message}"
                    )
                }
            }
        }
    }

    fun onCategorySelected(cat: FoodSubCategory) {
        _selectedSubCategory.value = cat
        launch(cat)
    }
}