package com.rabbitv.valheimviki.presentation.biome.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.BiomeFetchException
import com.rabbitv.valheimviki.domain.exceptions.BiomesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.BiomesInsertException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.ui_state.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.presentation.food.model.FoodListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject


@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    private val biomeUseCases: BiomeUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private  val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)




    val biomeUIState = combine(
        biomeUseCases.getLocalBiomesUseCase(),
        _error,
        _isLoading,
        _isConnection
    ){ value ->
        UIState<Biome>(
            list = value[0] as List<Biome>,
            error = value[1] as String?,
            isLoading = value[2] as Boolean,
            isConnection = value[3] as Boolean
        )
    }.onStart {
        _isLoading.value = true
        _error.value = null
    }
        .catch { e ->
            Log.e("BioemScreenVM", "getLocalBiomes failed", e)
            _isLoading.value = false
            _error.value     = e.message
            emit(
                UIState<Biome>(
                    list         = emptyList(),
                    error        = e.message,
                    isLoading    = false,
                    isConnection = _isConnection.value
                )
            )
        }
        .onEach {
            _isLoading.value = false
            _error.value = null
        }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(3000),
        UIState<Biome>()
    )

}