package com.rabbitv.valheimviki.presentation.detail.food.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.DataState
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiState
import com.rabbitv.valheimviki.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodUseCases: FoodUseCases,
    private val craftingObjectUseCases: CraftingObjectUseCases,
    private val relationUseCases: RelationUseCases,
    private val savedStateHandle: SavedStateHandle
    ): ViewModel(){
    private val _foodId :String = checkNotNull(savedStateHandle[Constants.FOOD_KEY])
    private val _food = MutableStateFlow<Food?>(null)
    private val _craftingCookingStation = MutableStateFlow<CraftingObject?>(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _error: MutableStateFlow<String?> = MutableStateFlow(null)


    val uiState :StateFlow<FoodDetailUiState> = combine(
        _food,
        _craftingCookingStation,
        _isLoading,
        _error
    ) {food,craftingCookingStation,isLoading,error ->
        FoodDetailUiState(
            food = food,
            craftingCookingStation = craftingCookingStation,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FoodDetailUiState()
    )


    init {
        loadFoodData()
    }

    internal fun loadFoodData(){

        viewModelScope.launch(Dispatchers.IO){
            try {
                _isLoading.value = true

                launch { _food.value = foodUseCases.getFoodByIdUseCase(_foodId).first() }
                val relatedIds = async { relationUseCases.getRelatedIdsUseCase(_foodId).first().map { it.id } }.await()


                _craftingCookingStation.value = async { craftingObjectUseCases.getCraftingObjectByIds(relatedIds).first() }.await()
            }catch (e:Exception)
            {
                Log.e("General fetch error FoodDetailViewModel", e.message.toString())
                _isLoading.value = false
                _error.value = e.message
            }finally {
                _isLoading.value = false
            }
        }
    }
    }