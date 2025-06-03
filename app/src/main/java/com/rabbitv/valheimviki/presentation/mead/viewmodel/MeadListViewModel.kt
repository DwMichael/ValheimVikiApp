package com.rabbitv.valheimviki.presentation.mead.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
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
class MeadListViewModel @Inject constructor(
    private val meadUseCases: MeadUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _selectedSubCategory =
        MutableStateFlow<MeadSubCategory>(MeadSubCategory.MEAD_BASE)

    private val _meadList: StateFlow<List<Mead>> =
        combine(
            meadUseCases.getLocalMeadsUseCase(),
            _selectedSubCategory,
        ) { allMead, category ->
            allMead
                .filter { it.subCategory == category.toString() }
        }.flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())


    val uiState: StateFlow<UiCategoryState<MeadSubCategory, Mead>> = combine(
        _meadList,
        _selectedSubCategory,
        connectivityObserver.isConnected.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    ) { meadList, selectedSubCategory, isConnected ->
        if (isConnected) {
            UiCategoryState.Success(selectedSubCategory, meadList)
        } else {
            if (meadList.isNotEmpty()) {
                UiCategoryState.Success(selectedSubCategory, meadList)
            } else {
                UiCategoryState.Error(
                    selectedSubCategory,
                    "No internet connection and no local data available for the selected filters."
                )
            }
        }

    }.onStart {
        emit(UiCategoryState.Loading(_selectedSubCategory.value))
    }.catch { e ->
        Log.e("MeadListVM", "Error in uiState flow", e)
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

    fun onCategorySelected(cat: MeadSubCategory) {
        _selectedSubCategory.value = cat
    }
}