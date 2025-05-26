package com.rabbitv.valheimviki.presentation.points_of_interest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.presentation.points_of_interest.model.PoiListUiState
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
class PoiListViewModel @Inject constructor(
    private val pointOfInterestUseCases: PointOfInterestUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _selectedSubCategory =
        MutableStateFlow<PointOfInterestSubCategory>(PointOfInterestSubCategory.FORSAKEN_ALTAR)


    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val _poiList: StateFlow<List<PointOfInterest>> =
        combine(
            pointOfInterestUseCases.getLocalPointOfInterestUseCase(),
            _selectedSubCategory,
        ) { all, category ->
            all
                .filter { it.subCategory == category.toString() }
        }
            .flowOn(Dispatchers.Default)
            .onStart {
                _isLoading.value = true
                _error.value = null
            }
            .catch { e ->
                Log.e("PoiListVM", "getLocalPoi failed", e)
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
        _poiList,
        _selectedSubCategory,
        _isConnection,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        PoiListUiState(
            poiList = values[0] as List<PointOfInterest>,
            selectedSubCategory = values[1] as PointOfInterestSubCategory,
            isConnection = values[2] as Boolean,
            isLoading = values[3] as Boolean,
            error = values[4] as String?,
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        PoiListUiState()
    )

    fun onCategorySelected(cat: PointOfInterestSubCategory) {
        _selectedSubCategory.value = cat
    }
}