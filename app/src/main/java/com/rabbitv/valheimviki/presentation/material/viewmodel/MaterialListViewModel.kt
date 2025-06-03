package com.rabbitv.valheimviki.presentation.material.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state.UiCategoryChipState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.presentation.material.model.MaterialSegmentOption
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
class MaterialListViewModel @Inject constructor(
    private val materialUseCases: MaterialUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {


    private val _selectedSubCategory =
        MutableStateFlow<MaterialSubCategory?>(null)


    private val _selectedSubType =
        MutableStateFlow<MaterialSubType?>(null)

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val _materialList: StateFlow<List<Material>> =
        combine(
            materialUseCases.getLocalMaterials(),
            _selectedSubCategory,
            _selectedSubType
        ) { all, category, type ->
            all
                .filter { category == null || it.subCategory == category.toString() }
                .filter { type == null || it.subType == type.toString() }
                .sortedBy { it.order }
        }.flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())


    val uiState: StateFlow<UiCategoryChipState<MaterialSubCategory?, MaterialSubType?, Material>> =
        combine(
            _materialList,
            _selectedSubCategory,
            _selectedSubType,
            connectivityObserver.isConnected.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )
        ) { materials, selectedSubCategory, selectedSubType, isConnected ->
            if (isConnected) {
                if (materials.isNotEmpty()) {
                    UiCategoryChipState.Success(selectedSubCategory, selectedSubType, materials)
                } else {
                    UiCategoryChipState.Loading(selectedSubCategory, selectedSubType)
                }
            } else {
                if (materials.isNotEmpty()) {
                    UiCategoryChipState.Success(selectedSubCategory, selectedSubType, materials)
                } else {
                    UiCategoryChipState.Error(
                        selectedSubCategory, selectedSubType,
                        "No internet connection and no local data available. Try to connect to the internet again.",
                    )
                }
            }
        }.onStart {
            emit(UiCategoryChipState.Loading(_selectedSubCategory.value, _selectedSubType.value))
        }.catch { e ->
            Log.e("MaterialListVM", "Error in uiState flow", e)
            emit(
                UiCategoryChipState.Error(
                    _selectedSubCategory.value,
                    _selectedSubType.value,
                    e.message ?: "An unknown error occurred"
                )
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            UiCategoryChipState.Loading(_selectedSubCategory.value, _selectedSubType.value)
        )

    fun onCategorySelected(cat: MaterialSubCategory?) {
        _selectedSubCategory.value = cat
    }

    fun onTypeSelected(type: MaterialSubType?) {
        _selectedSubType.value = type
    }

    fun getLabelFor(subCategory: MaterialSubCategory): String =
        MaterialSegmentOption.entries
            .first { it.value == subCategory }
            .label
}