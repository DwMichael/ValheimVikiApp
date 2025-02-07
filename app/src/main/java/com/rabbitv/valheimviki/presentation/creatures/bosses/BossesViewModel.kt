package com.rabbitv.valheimviki.presentation.creatures.bosses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.creature.Type
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val creatureUseCases: CreatureUseCases
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
                creatureUseCases.getBossesUseCase("en").collect { bosses ->
                    _bossUIState.update { current ->
                        current.copy(bosses = bosses, isLoading = false)
                    }
                }
            } catch (e: FetchException) {
                _bossUIState.value = _bossUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _bossUIState.value = _bossUIState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
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