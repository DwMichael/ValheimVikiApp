package com.rabbitv.valheimviki.presentation.crafting.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class CraftingListViewModel @Inject constructor(
    private val craftingObjectUseCases: CraftingObjectUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {


    private val _selectedChip = MutableStateFlow<CraftingSubCategory?>(null)


    private val _craftingObjects: StateFlow<List<CraftingObject>> =
        combine(
            craftingObjectUseCases.getLocalCraftingObjectsUseCase(),
            _selectedChip,
        ) { allCraftingObjects, chip ->
            val filtered = allCraftingObjects.filter { chip == null || it.subCategory == chip.toString() }

            if (chip == null) {
                filtered.sortedBy { it.name }
            } else {
                filtered.sortedBy { it.order }
            }
        }.flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<UiCategoryState<CraftingSubCategory?, CraftingObject>> = combine(
        _craftingObjects,
        _selectedChip,
        connectivityObserver.isConnected.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = false
        )
    ) { craftingObjects, selectedChip, isConnected ->
        if (isConnected) {
            if (craftingObjects.isNotEmpty()) {
                UiCategoryState.Success(selectedChip, craftingObjects)
            } else {
                UiCategoryState.Loading(selectedChip)
            }
        } else {
            if (craftingObjects.isNotEmpty()) {
                UiCategoryState.Success(selectedChip, craftingObjects)
            } else {
                UiCategoryState.Error(
                    selectedChip,
                    "No internet connection and no local data available. Try to connect to the internet again.",
                )
            }
        }
    }.onStart {
        emit(UiCategoryState.Loading(_selectedChip.value))
    }.catch { e ->
        Log.e("CraftingObjectListVM", "Error in uiState flow", e)
        emit(
            UiCategoryState.Error(
                _selectedChip.value,
                e.message ?: "An unknown error occurred"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = UiCategoryState.Loading(_selectedChip.value)
    )


    fun onChipSelected(chip: CraftingSubCategory?) {
        _selectedChip.value = chip
    }
}