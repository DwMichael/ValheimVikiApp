package com.rabbitv.valheimviki.presentation.creatures.bosses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBosses
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.utils.Constants.DEFAULT_LANG
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
class BossesViewModel @Inject constructor(
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


    private val _bossUiState = MutableStateFlow(BossUiState())
    val bossUIState: StateFlow<BossUiState> = _bossUiState

    private val _scrollPosition = MutableStateFlow(0)
    val scrollPosition: StateFlow<Int> = _scrollPosition

    fun saveScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    init {
        load()
    }

    private fun load() {
        _bossUiState.value = _bossUiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                creatureUseCases.getMainBossesUseCase().collect { bosses ->
                    _bossUiState.update { current ->
                        current.copy(bosses = bosses, isLoading = false)
                    }
                }
            } catch (e: CreatureFetchException) {
                _bossUiState.value = _bossUiState.value.copy(isLoading = false, error = e.message)
            } catch (e: FetchException) {
                _bossUiState.value = _bossUiState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _bossUiState.value = _bossUiState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }

    fun refetchBosses() {
        _bossUiState.value = _bossUiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                creatureUseCases.refetchCreaturesUseCase(DEFAULT_LANG, RefetchUseCases.GET_BOSSES)
                    .collect { sortedBosses ->
                        _bossUiState.update { current ->
                            current.copy(bosses = sortedBosses.toMainBosses(), isLoading = false)
                        }
                    }
            } catch (e: FetchException) {
                _bossUiState.value = _bossUiState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _bossUiState.value = _bossUiState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }


}