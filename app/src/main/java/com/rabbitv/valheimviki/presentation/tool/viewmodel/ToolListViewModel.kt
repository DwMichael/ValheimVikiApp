package com.rabbitv.valheimviki.presentation.tool.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.tool.Tool
import com.rabbitv.valheimviki.domain.model.tool.ToolSubCategory
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.presentation.tool.model.ToolListUiState
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
class ToolListViewModel @Inject constructor(
    private val toolUseCases: ToolUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _selectedChip =
        MutableStateFlow<ToolSubCategory?>(null)


    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val _toolList: StateFlow<List<Tool>> =
        combine(
            toolUseCases.getLocalToolsUseCase(),
            _selectedChip,
        ) { allTools, category ->
            if (category == null) allTools
            else allTools.filter { it.subCategory == category.name }
        }
            .flowOn(Dispatchers.Default)
            .onStart {
                _isLoading.value = true
                _error.value = null
            }
            .catch { e ->
                Log.e("ToolListVM", "getLocalTool failed", e)
                _isLoading.value = false
                _error.value = e.message
                emit(emptyList())
            }
            .onEach {
                _isLoading.value = false
                _error.value = null
            }
            .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())


    val uiState = combine(
        _toolList,
        _selectedChip,
        _isConnection,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        ToolListUiState(
            toolList = values[0] as List<Tool>,
            selectedChip = values[1] as ToolSubCategory?,
            isConnection = values[2] as Boolean,
            isLoading = values[3] as Boolean,
            error = values[4] as String?,
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        ToolListUiState()
    )

    fun onChipSelected(cat: ToolSubCategory?) {
        _selectedChip.value = cat
    }
}