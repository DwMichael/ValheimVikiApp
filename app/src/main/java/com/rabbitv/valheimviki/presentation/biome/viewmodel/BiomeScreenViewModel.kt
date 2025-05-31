package com.rabbitv.valheimviki.presentation.biome.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.UiListState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    private val biomeUseCases: BiomeUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {

    val biomeUiListState: StateFlow<UiListState<Biome>> = combine(
        biomeUseCases.getLocalBiomesUseCase()
            .catch { e ->
                Log.e("BiomeScreenVM", "getLocalBiomesUseCase failed in combine", e)
                emit(emptyList())
            },
        connectivityObserver.isConnected
    ) { biomes, isConnected ->
        if (isConnected) {
            if (biomes.isNotEmpty()) {
                UiListState.Success(biomes)
            } else {
                UiListState.Loading
            }
        } else {
            if (biomes.isNotEmpty()) {
                UiListState.Success(biomes)
            } else {
                UiListState.Error("No internet connection and no local data available.")
            }
        }
    }.onStart {
        emit(UiListState.Loading)
    }.catch { e ->
        Log.e("BiomeScreenVM", "Error in biomeUiState flow", e)
        emit(UiListState.Error(e.message ?: "An unknown error occurred"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000),
        initialValue = UiListState.Loading
    )

}