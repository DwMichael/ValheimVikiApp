package com.rabbitv.valheimviki.presentation.building_material.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state.UiCategoryChipState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialSegmentOption
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
class BuildingMaterialListViewModel @Inject constructor(
    private val buildingMaterialUseCases: BuildMaterialUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _selectedSubCategory =
        MutableStateFlow<BuildingMaterialSubCategory?>(null)


    private val _selectedSubType =
        MutableStateFlow<BuildingMaterialSubType?>(null)

    private val _materialList: StateFlow<List<BuildingMaterial>> =
        combine(
            buildingMaterialUseCases.getLocalBuildMaterial(),
            _selectedSubCategory,
            _selectedSubType
        ) { all, category, type ->
            all
                .filter { category == null || it.subCategory == category.toString() }
                .filter { type == null || it.subType == type.toString() }
                .sortedBy { it.order }
        }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())


    val uiState: StateFlow<UiCategoryChipState<BuildingMaterialSubCategory?, BuildingMaterialSubType?, BuildingMaterial>> =
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
            Log.e("BuildingMaterialListVM", "Error in uiState flow", e)
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

    fun onCategorySelected(cat: BuildingMaterialSubCategory?) {
        _selectedSubCategory.value = cat
    }

    fun onTypeSelected(type: BuildingMaterialSubType?) {
        _selectedSubType.value = type
    }


    fun getLabelFor(subCategory: BuildingMaterialSubCategory): String =
        BuildingMaterialSegmentOption.entries
            .first { it.value == subCategory }
            .label
}