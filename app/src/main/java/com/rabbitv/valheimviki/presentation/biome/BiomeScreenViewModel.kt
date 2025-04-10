package com.rabbitv.valheimviki.presentation.biome

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

data class BiomesUIState(
    val biomes: List<Biome> = emptyList(), val error: String? = null, val isLoading: Boolean = false
)

@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    private val relationsRepository: RelationUseCases,
    private val creatureUseCases: CreatureUseCases,
    private val biomeUseCases: BiomeUseCases,
    @ApplicationContext private val context: Context
) : ViewModel() {

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
                biomeUseCases.getAllBiomesUseCase("en").collect { sortedBiomes ->
                    _biomeUIState.update { current ->
                        current.copy(biomes = sortedBiomes, isLoading = false)
                    }
                }

            } catch (e: FetchException) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            creatureUseCases.fetchCreatureAndInsertUseCase("en")
        }
        viewModelScope.launch(Dispatchers.IO) {
            relationsRepository.fetchAndInsertRelationsUseCase()
        }
    }

    fun refetchBiomes() {
        _biomeUIState.value = _biomeUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch(Dispatchers.IO) {
            if (!isNetworkAvailable(context)) {
                _biomeUIState.value = _biomeUIState.value.copy(
                    isLoading = false, error = "No internet connection"
                )
                _isRefreshing.emit(false)
                return@launch
            }

            try {
                biomeUseCases.refetchBiomesUseCase("en").collect { sortedBiomes ->
                    _biomeUIState.update { current ->
                        current.copy(biomes = sortedBiomes, isLoading = false)
                    }
                }
            } catch (e: FetchException) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }
}