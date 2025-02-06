package com.rabbitv.valheimviki.presentation.creatures.mini_bosses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.Type
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MiniBossesUIState(
    val creatures: List<CreatureDtoX> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)


@HiltViewModel
class MiniBossesViewModel @Inject constructor(
    private val creatureRepository: CreatureRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _miniBossesUIState = MutableStateFlow(MiniBossesUIState())
    val miniBossesUIState: StateFlow<MiniBossesUIState> = _miniBossesUIState


    init {
        load()
    }

    fun load() {
        _miniBossesUIState.value = _miniBossesUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = creatureRepository.fetchCreatures("en")
                when (response.success) {
                    true -> {
                        creatureRepository.getMiniBosses()
                            .map { creatureList ->
                                creatureList.sortedWith(
                                    compareBy<CreatureDtoX> { creature ->
                                        typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                                    }
                                        .thenBy { it.order }
                                )
                            }
                            .collect { sortedCreatures ->
                                _miniBossesUIState.update { current ->
                                    current.copy(
                                        creatures = sortedCreatures,
                                        isLoading = false,
                                        error = current.error
                                    )
                                }
                            }
                    }

                    false -> {
                        _miniBossesUIState.value =
                            _miniBossesUIState.value.copy(isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _miniBossesUIState.value =
                    _miniBossesUIState.value.copy(isLoading = false, error = e.message)
            }
            _isRefreshing.emit(false)
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