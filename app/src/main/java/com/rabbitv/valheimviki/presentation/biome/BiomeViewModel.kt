package com.rabbitv.valheimviki.presentation.biome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.remote.api.BiomeRepository
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.Stage
import com.rabbitv.valheimviki.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@HiltViewModel
class BiomeViewModel @Inject constructor(
    private val biomeRepository: BiomeRepository
) :ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<BiomeDtoX>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<BiomeDtoX>>> = _uiState


    init {
        refreshBiomes()
        fetchBiomeRespond()
    }

    private fun refreshBiomes() {
        viewModelScope.launch {
            try {
                biomeRepository.refreshBiomes("en")
            }catch (e:Exception)
            {
                _uiState.value = UiState.Error(e.toString())
            }
        }
    }

    private fun fetchBiomeRespond() {

            viewModelScope.launch {
                biomeRepository.getAllBiomes("en")
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.map {
                    biomeList ->
                    biomeList.sortedWith(
                        compareBy<BiomeDtoX> {
                            biome ->
                            stageOrderMap.getOrElse(biome.stage){Int.MAX_VALUE}
                        }
                            .thenBy { it.order }
                    )
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    private val stageOrderMap = mapOf(
        Stage.EARLY.toString() to 1,
        Stage.MID.toString()   to 2,
        Stage.LATE.toString()  to 3
    )


}