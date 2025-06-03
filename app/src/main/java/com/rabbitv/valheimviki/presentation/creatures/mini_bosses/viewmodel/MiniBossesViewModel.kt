package com.rabbitv.valheimviki.presentation.creatures.mini_bosses.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.UiListState
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
class MiniBossesViewModel @Inject constructor(
    private val creatureUseCases: CreatureUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {

    val miniBossUiListState: StateFlow<UiListState<MiniBoss>> = combine(
        creatureUseCases.getMiniBossesUseCase()
            .catch { e ->
                Log.e("MiniBossScreenVM", "getLocalCreaturesUseCase failed in combine", e)
                emit(emptyList())
            },
        connectivityObserver.isConnected.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = true
        )
    ) { creatures, isConnected ->
        if (isConnected) {
            if (creatures.isNotEmpty()) {
                UiListState.Success(creatures)
            } else {
                UiListState.Loading
            }
        } else {
            if (creatures.isNotEmpty()) {
                UiListState.Success(creatures)
            } else {
                UiListState.Error("No internet connection and no local data available.")
            }
        }
    }.onStart {
        emit(UiListState.Loading)
    }.catch { e ->
        Log.e("MiniBossScreenVM", "Error in creatureUiState flow", e)
        emit(UiListState.Error(e.message ?: "An unknown error occurred"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(3000),
        initialValue = UiListState.Loading
    )


}