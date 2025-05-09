package com.rabbitv.valheimviki.presentation.biome

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.BiomeFetchException
import com.rabbitv.valheimviki.domain.exceptions.BiomesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.BiomesInsertException
import com.rabbitv.valheimviki.domain.exceptions.RelationFetchException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

data class BiomesUIState(
    val biomes: List<Biome> = emptyList(),
    val scrollPosition: Int = 0,
    val saveScrollPosition: (Int) -> Unit = {},
    val areCreatures: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    private val biomeUseCases: BiomeUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    val isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _scrollPosition = MutableStateFlow(0)
    val scrollPosition: StateFlow<Int> = _scrollPosition

    fun saveScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _biomeUIState = MutableStateFlow(BiomesUIState())
    val biomeUIState: StateFlow<BiomesUIState> = _biomeUIState

    init {
        load()
    }

    @VisibleForTesting
    internal fun load() {
        _biomeUIState.value = _biomeUIState.value.copy(isLoading = true, error = null)


        viewModelScope.launch(Dispatchers.IO) {
            try {
                biomeUseCases.getLocalBiomesUseCase().collect { sortedBiomes ->
                    _biomeUIState.update { current ->
                            current.copy(biomes = sortedBiomes, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is BiomeFetchException -> Log.e(
                        "BiomeFetchException BiomeScreenViewModel",
                        "${e.message}"
                    )

                    is BiomesInsertException -> Log.e(
                        "BiomesInsertException BiomeScreenViewModel",
                        "${e.message}"
                    )

                    is BiomesFetchLocalException -> Log.e(
                        "BiomesFetchLocalException BiomeScreenViewModel",
                        "${e.message}"
                    )

                    else -> Log.e(
                        "Unexpected Exception occurred BiomeScreenViewModel",
                        "${e.message}"
                    )
                }
            }
        }

    }


    fun refetchBiomes() {
        _biomeUIState.value = _biomeUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch(Dispatchers.IO) {
            if (isConnection.value) {
                _biomeUIState.value = _biomeUIState.value.copy(
                    isLoading = false, error = "No internet connection"
                )
                _isRefreshing.emit(false)
                return@launch
            }

            try {
                load()
            } catch (e: BiomeFetchException) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: BiomesInsertException) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: RelationFetchException) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }
}