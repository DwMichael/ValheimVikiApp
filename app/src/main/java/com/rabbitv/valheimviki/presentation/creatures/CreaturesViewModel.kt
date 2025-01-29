package com.rabbitv.valheimviki.presentation.creatures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.remote.api.CreatureRepository
import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.Type
import com.rabbitv.valheimviki.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreaturesViewModel @Inject constructor(
    private val creatureRepository: CreatureRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<CreatureDtoX>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<CreatureDtoX>>> = _uiState


    init {
        refreshCreatures()
        fetchCreatureRespond()
    }

    private fun refreshCreatures() {
        viewModelScope.launch {
            try {
                creatureRepository.refreshCreatures("en")
            }catch (e:Exception)
            {
                _uiState.value = UiState.Error(e.toString())
            }
        }
    }

    private fun fetchCreatureRespond() {

        viewModelScope.launch {
            creatureRepository.getAllCreatures("pl")
                .catch { e ->
                _uiState.value = UiState.Error(e.toString())
            }.map { creatureList ->
                    creatureList.sortedWith(
                        compareBy<CreatureDtoX> { creature ->
                            typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                        }
                            .thenBy { it.order }
                    )
                }.collect {
                    _uiState.value = UiState.Success(it)
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