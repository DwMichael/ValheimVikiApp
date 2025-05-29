package com.rabbitv.valheimviki.presentation.creatures.bosses.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.ui_state.UiState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BossesViewModel @Inject constructor(
    private val creatureUseCases: CreatureUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {


    val mainBossUiState: StateFlow<UiState<MainBoss>> = combine(
        creatureUseCases.getMainBossesUseCase()
            .catch { e ->
                Log.e("BossScreenVM", "getLocalCreaturesUseCase failed in combine", e)
                emit(emptyList())
            },
        connectivityObserver.isConnected
    ) { creatures, isConnected ->
        if (isConnected) {
            if (creatures.isNotEmpty()) {
                UiState.Success(creatures)
            } else {
                UiState.Loading
            }
        } else {
            if (creatures.isNotEmpty()) {
                UiState.Success(creatures)
            } else {
                UiState.Error("No internet connection and no local data available.")
            }
        }
    }.onStart {
        emit(UiState.Loading)
    }.catch { e ->
        Log.e("BossScreenVM", "Error in creatureUiState flow", e)
        emit(UiState.Error(e.message ?: "An unknown error occurred"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(3000),
        initialValue = UiState.Loading
    )


}