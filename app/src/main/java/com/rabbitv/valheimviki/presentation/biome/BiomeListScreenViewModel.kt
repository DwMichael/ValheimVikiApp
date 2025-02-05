package com.rabbitv.valheimviki.presentation.biome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.Stage
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BiomesUIState(
    val biomes: List<BiomeDtoX> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class BiomeListScreenViewModel @Inject constructor(
    private val biomeRepository: BiomeRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _biomeUIState = MutableStateFlow(BiomesUIState())
    val biomeUIState: StateFlow<BiomesUIState> = _biomeUIState

    init {
        load()
    }

    fun load() {
        _biomeUIState.value = _biomeUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = biomeRepository.refreshBiomes("en")

                when (response.success) {
                    true -> {
                        biomeRepository.getAllBiomes()
                            .map { biomes ->
                                biomes.sortedWith(
                                    compareBy(
                                        { stageOrderMap[it.stage] ?: Int.MAX_VALUE },
                                        { it.order }
                                    )
                                )
                            }
                            .collect { sortedBiomes ->
                                _biomeUIState.update { current ->
                                    current.copy(
                                        biomes = sortedBiomes,
                                        isLoading = false,
                                        error = null
                                    )
                                }
                            }
                    }

                    false -> {
                        _biomeUIState.value =
                            _biomeUIState.value.copy(isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _biomeUIState.value =
                    _biomeUIState.value.copy(isLoading = false, error = e.message)
            }

            _isRefreshing.emit(false)
        }
    }

    private val stageOrderMap = mapOf(
        Stage.EARLY.toString() to 1,
        Stage.MID.toString() to 2,
        Stage.LATE.toString() to 3
    )
}