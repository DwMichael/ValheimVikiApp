package com.rabbitv.valheimviki.presentation.creatures.bosses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.creature.Type
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BossUIState(
    val bosses: List<CreatureDtoX> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)


@HiltViewModel
class BossesViewModel @Inject constructor(
    private val creatureRepository: CreatureRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _bossUIState = MutableStateFlow(BossUIState())
    val bossUIState: StateFlow<BossUIState> = _bossUIState


    init {
        load()
    }

    fun load() {
        _bossUIState.value = _bossUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val response = creatureRepository.fetchCreatures("en")
                var errorMessage = response.error
                if (response.errorDetails == "503") {
                    errorMessage = "Server Unavailable try later"
                }
                creatureRepository.getMainBosses()
                    .map { creatureList ->
                        creatureList.sortedWith(
                            compareBy<CreatureDtoX> { creature ->
                                typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                            }
                                .thenBy { it.order }
                        )
                    }
                    .collect { sortedCreatures ->
                        _bossUIState.update { current ->
                            current.copy(
                                bosses = sortedCreatures,
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                    }
            } catch (e: Exception) {
                _bossUIState.value =
                    _bossUIState.value.copy(isLoading = false, error = e.message)
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