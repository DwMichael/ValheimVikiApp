package com.rabbitv.valheimviki.presentation.creatures.mini_bosses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MiniBossesUIState(
    val miniBosses: List<MainBoss> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)


@HiltViewModel
class MiniBossesViewModel @Inject constructor(
    private val creatureUseCases: CreatureUseCases,
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _miniBossesUIState = MutableStateFlow(MiniBossesUIState())
    val miniBossesUIState: StateFlow<MiniBossesUIState> = _miniBossesUIState


    init {
        load()
    }

    private fun load() {
        _miniBossesUIState.value = _miniBossesUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                creatureUseCases.getMainBossesUseCase("en").collect { miniBoss ->
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
        viewModelScope.launch {
            try {
                creatureUseCases.refetchCreaturesUseCase("en", RefetchUseCases.GET_BOSSES)
                    .collect { sortedMiniBosses ->
                        _miniBossesUIState.update { current ->
                            current.copy(miniBosses = sortedMiniBosses, isLoading = false)
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