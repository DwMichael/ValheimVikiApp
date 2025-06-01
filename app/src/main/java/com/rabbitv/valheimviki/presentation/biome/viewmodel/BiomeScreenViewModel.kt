package com.rabbitv.valheimviki.presentation.biome.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.ErrorType
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.UiListState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    private val biomeUseCases: BiomeUseCases,
    private val connectivityObserver: NetworkConnectivity,
    ) : ViewModel() {

    val biomeUiListState: StateFlow<UiListState<Biome>> = combine(
        biomeUseCases.getLocalBiomesUseCase(),
        connectivityObserver.isConnected,
    ) { biomes, isConnected ->
        if (isConnected) {
            if (biomes.isNotEmpty()) {
                UiListState.Success(biomes)
            } else {
                UiListState.Loading
                delay(30_000L)
                UiListState.Error(
                    "A problem occurred while connecting to our server.\n We are working to resolve it as quickly as possible. Please try again in a moment.",
                    ErrorType.SERVER_DOWN
                )
            }
        } else {
            if (biomes.isNotEmpty()) {
                UiListState.Success(biomes)
            } else {
                UiListState.Error(
                    "No internet connection and no local data available. Try to connect to the internet again.",
                    ErrorType.INTERNET_CONNECTION
                )
            }
        }
    }.onStart {
        emit(UiListState.Loading)
    }.catch { e ->
        val errorMessage = when (e) {
            is IOException -> "Problem accessing local data."
            else -> "An unexpected error occurred."
        }
        emit(UiListState.Error(errorMessage, ErrorType.UNKNOWN_ERROR))
        Log.e("BiomeScreenVM", "Error in biomeUiState flow", e)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000),
        initialValue = UiListState.Loading
    )

}