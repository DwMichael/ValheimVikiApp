package com.rabbitv.valheimviki.presentation.biome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.Stage
import com.rabbitv.valheimviki.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BiomeListScreenViewModel @Inject constructor(
    private val biomeRepository: BiomeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<BiomeDtoX>>>(UiState())
    val uiState: StateFlow<UiState<List<BiomeDtoX>>> = _uiState

    init {
        fetchData()
    }

    fun fetchData() {
        fetchBiomesFromAPI()
        fetchBiomesFromRoom()
    }




    private fun fetchBiomesFromAPI() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch {
            try {
                biomeRepository.refreshBiomes("en")
            } catch (e: Exception) {
                _uiState.value = UiState(isLoading = false)
//                _uiState.update { current ->
//                    if (current.data == null) {
//                        current.copy(
//                            isLoading = false,
//                            error = e.message ?: "Error fetching data"
//                        ).takeUnless { current.shouldShowData }
//                            ?: current.copy(error = e.message ?: "Error fetching data")
//
//                    }else{
//                        current.copy(isLoading = false)
//                    }
//                }
            }
            _uiState.value = UiState(isLoading = false)
        }

    }

    private fun fetchBiomesFromRoom() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch {
            biomeRepository.getAllBiomes()
                .catch { e ->
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            error = e.message ?: "Local data error")
                    }
                }
                .map { biomes ->
                    biomes.sortedWith(
                        compareBy(
                            { stageOrderMap[it.stage] ?: Int.MAX_VALUE },
                            { it.order }
                        )
                    )
                }
                .collect { sortedBiomes ->
                    _uiState.update { current ->
                        val shouldClearError = sortedBiomes.isNotEmpty()

                        current.copy(
                            data = sortedBiomes,
                            isLoading = false,
                            error = if (shouldClearError) null else current.error ?: "Connect to internet to fetch data"
                        )
                    }
                }
        }
        _uiState.value = UiState(isLoading = false)
    }

    private val stageOrderMap = mapOf(
        Stage.EARLY.toString() to 1,
        Stage.MID.toString() to 2,
        Stage.LATE.toString() to 3
    )
}