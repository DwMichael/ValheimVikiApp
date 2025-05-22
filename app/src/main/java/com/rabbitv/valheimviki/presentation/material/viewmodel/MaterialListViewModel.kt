package com.rabbitv.valheimviki.presentation.material.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.presentation.material.model.MaterialListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MaterialListViewModel @Inject constructor(
    private val materialUseCases: MaterialUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

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
        }
            .flowOn(Dispatchers.Default)
            .onStart {
                _isLoading.value = true
                _error.value = null
            }.catch { e ->
                Log.e("MaterialListVM", "getLocalMaterial failed", e)
                _isLoading.value = false
                _error.value = e.message
                emit(emptyList())
            }.onEach {
                _isLoading.value = false
                _error.value = null
            }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())


    val uiState = combine(
        _materialList,
        _selectedSubCategory,
        _selectedSubType,
        _isConnection,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        MaterialListUiState(
            materialsList = values[0] as List<Material>,
            selectedSubCategory = values[1] as MaterialSubCategory?,
            selectedSubType = values[2] as MaterialSubType?,
            isConnection = values[3] as Boolean,
            isLoading = values[4] as Boolean,
            error = values[5] as String?,
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        MaterialListUiState()
    )

    fun onCategorySelected(cat: MaterialSubCategory?) {
        Log.e("MATERIALSUBCATEGOYR ", "onCategorySelected: $cat")
        _selectedSubCategory.value = cat
    }

    fun onTypeSelected(type: MaterialSubType?) {
        _selectedSubType.value = type
    }
}