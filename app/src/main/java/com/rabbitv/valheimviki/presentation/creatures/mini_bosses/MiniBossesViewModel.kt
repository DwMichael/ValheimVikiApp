package com.rabbitv.valheimviki.presentation.creatures.mini_bosses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMiniBosses
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MiniBossesViewModel @Inject constructor(
    private val creatureUseCases: CreatureUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {

    val isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )



    private val _miniBossesUiState = MutableStateFlow(MiniBossesUiState())
    val miniBossesUIState: StateFlow<MiniBossesUiState> = _miniBossesUiState

    private val _scrollPosition = MutableStateFlow(0)
    val scrollPosition: StateFlow<Int> = _scrollPosition

    fun saveScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    init {
        load()
    }

    private fun load() {
        _miniBossesUiState.value = _miniBossesUiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                creatureUseCases.getMiniBossesUseCase("en").collect { miniBoss ->
                    _miniBossesUiState.update { current ->
                        current.copy(miniBosses = miniBoss, isLoading = false)
                    }
                }
            } catch (e: FetchException) {
                _miniBossesUiState.value =
                    _miniBossesUiState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _miniBossesUiState.value =
                    _miniBossesUiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }


}