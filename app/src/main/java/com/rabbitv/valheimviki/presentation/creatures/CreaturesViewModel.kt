package com.rabbitv.valheimviki.presentation.creatures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.Type
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreaturesViewModel @Inject constructor(
    private val creatureRepository: CreatureRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<CreatureDtoX>>>(UiState(isLoading = true))
    val uiState: StateFlow<UiState<List<CreatureDtoX>>> = _uiState


    init {

        fetchInitialData()
    }

    private fun fetchInitialData() {
        fetchBiomesFromRoom()
        fetchCreaturesFromAPI()
    }

    fun fetchCreaturesFromAPI(showLoading: Boolean = true) {
        if (showLoading) {
            _uiState.update { it.copy(isLoading = true) }
        }

        viewModelScope.launch {
            try {
                creatureRepository.refreshCreatures("en")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        error = e.message ?: "Error fetching data"
                    )
                }
            }
        }
    }
//    private fun refreshCreatures() {
//        viewModelScope.launch {
//            try {
//                creatureRepository.refreshCreatures("en")
//            }catch (e:Exception)
//            {
//                _uiState.value = UiState.Error(e.toString())
//            }
//        }
//    }
//private fun fetchCreaturesFromRoom() {
//
//    viewModelScope.launch {
//        creatureRepository.getAllCreatures("pl")
//            .catch { e ->
//                _uiState.value = UiState.Error(e.toString())
//            }.map { creatureList ->
//                creatureList.sortedWith(
//                    compareBy<CreatureDtoX> { creature ->
//                        typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
//                    }
//                        .thenBy { it.order }
//                )
//            }.collect {
//                _uiState.value = UiState.Success(it)
//            }
//    }
//
//}

    private fun fetchBiomesFromRoom() {
        viewModelScope.launch {
            creatureRepository.getAllCreatures()
                .catch { e ->
                    _uiState.update { current ->
                        current.copy(error = e.message ?: "Local data error")
                    }
                }
                .map { creatureList ->
                    creatureList.sortedWith(
                        compareBy<CreatureDtoX> { creature ->
                            typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                        }
                            .thenBy { it.order }
                    )
                }
                .collect { sortedBiomes ->
                    _uiState.update { current ->
                        val shouldClearError = sortedBiomes.isNotEmpty()
                        current.copy(
                            data = sortedBiomes,
                            error = if (shouldClearError) null else current.error
                        )
                    }
                }
        }
    }

    private val typeOrderMap = mapOf(
        Type.BOSS.toString() to 1,
        Type.MINI_BOSS.toString() to 2,
        Type.AGGRESSIVE_CREATURE.toString() to 3,
        Type.PASSIVE_CREATURE.toString() to 4,
        Type.NPC.toString() to 5,
    )
}