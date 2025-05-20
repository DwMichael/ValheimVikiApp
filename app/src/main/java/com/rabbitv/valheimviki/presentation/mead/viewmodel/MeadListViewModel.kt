package com.rabbitv.valheimviki.presentation.mead.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.presentation.mead.model.MeadListUiState
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
class MeadListViewModel @Inject constructor(
    private val meadUseCases: MeadUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _selectedSubCategory =
        MutableStateFlow<MeadSubCategory>(MeadSubCategory.MEAD_BASE)


    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val _meadList: StateFlow<List<Mead>> =
        combine(
            meadUseCases.getLocalMeadsUseCase(),
            _selectedSubCategory,
        ) { allMead, category ->
            allMead
                .filter { it.subCategory == category.toString() }
        }
            .flowOn(Dispatchers.Default)
            .onStart {
                _isLoading.value = true
                _error.value = null
            }
            .catch { e ->
                Log.e("MeadListVM", "getLocalMead failed", e)
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
        _meadList,
        _selectedSubCategory,
        _isConnection,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        MeadListUiState(
            meadList = values[0] as List<Mead>,
            selectedSubCategory = values[1] as MeadSubCategory,
            isConnection = values[2] as Boolean,
            isLoading = values[3] as Boolean,
            error = values[4] as String?,
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        MeadListUiState()
    )

    fun onCategorySelected(cat: MeadSubCategory) {
        _selectedSubCategory.value = cat
    }
}