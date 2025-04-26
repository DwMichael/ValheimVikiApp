package com.rabbitv.valheimviki.presentation.creatures.mini_bosses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMiniBosses
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
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

data class MiniBossesUIState(
    val miniBosses: List<MiniBoss> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)


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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _miniBossesUIState = MutableStateFlow(MiniBossesUIState())
    val miniBossesUIState: StateFlow<MiniBossesUIState> = _miniBossesUIState

    private val _scrollPosition = MutableStateFlow(0)
    val scrollPosition: StateFlow<Int> = _scrollPosition

    fun saveScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    init {
        load()
    }

    private fun load() {
        _miniBossesUIState.value = _miniBossesUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                creatureUseCases.getMiniBossesUseCase("en").collect { miniBoss ->
                    _miniBossesUIState.update { current ->
                        current.copy(miniBosses = miniBoss, isLoading = false)
                    }
                }
            } catch (e: FetchException) {
                _miniBossesUIState.value =
                    _miniBossesUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _miniBossesUIState.value =
                    _miniBossesUIState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }

    fun refetchBiomes() {
        _miniBossesUIState.value = _miniBossesUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                creatureUseCases.refetchCreaturesUseCase("en", RefetchUseCases.GET_MINI_BOSSES)
                    .collect { sortedMiniBosses ->
                        _miniBossesUIState.update { current ->
                            current.copy(miniBosses = sortedMiniBosses.toMiniBosses(), isLoading = false)
                        }
                    }
            } catch (e: FetchException) {
                _miniBossesUIState.value =
                    _miniBossesUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _miniBossesUIState.value =
                    _miniBossesUIState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }

}