package com.rabbitv.valheimviki.presentation.armor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
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
class ArmorListViewModel @Inject constructor(
    private val armorRepository: ArmorRepository,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {


    private val _selectedChip = MutableStateFlow<ArmorSubCategory?>(null)


    private val _armors: StateFlow<List<Armor>> =
        combine(
            armorRepository.getLocalArmors(),
            _selectedChip,
        ) { allArmors, chip ->
            allArmors
                .filter { chip == null || it.subCategory == chip.toString() }
                .sortedBy { it.order }
        }.flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<UiCategoryState<ArmorSubCategory?, Armor>> = combine(
        _armors,
        _selectedChip,
        connectivityObserver.isConnected.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = false
        )
    ) { armors, selectedChip, isConnected ->
        if (isConnected) {
            UiCategoryState.Success(selectedChip, armors)
        } else {
            if (armors.isNotEmpty()) {
                UiCategoryState.Success(selectedChip, armors)
            } else {
                UiCategoryState.Error(
                    selectedChip,
                    "No internet connection and no local data available for the selected filters."
                )
            }
        }
    }.onStart {
        emit(UiCategoryState.Loading(_selectedChip.value))
    }.catch { e ->
        Log.e("ArmorListVM", "Error in uiState flow", e)
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


    fun onChipSelected(chip: ArmorSubCategory?) {
        _selectedChip.value = chip
    }
}