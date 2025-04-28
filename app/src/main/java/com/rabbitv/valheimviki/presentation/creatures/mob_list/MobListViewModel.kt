package com.rabbitv.valheimviki.presentation.creatures.mob_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MobListViewModel @Inject constructor(
    private val creatureuseCases: CreatureUseCases
) : ViewModel() {

    private val _aggressiveCreature = MutableStateFlow<List<AggressiveCreature>>(emptyList())

    private val _scrollPosition = MutableStateFlow(0)
    val scrollPosition: StateFlow<Int> = _scrollPosition

    fun saveScrollPosition(position: Int) {
        _scrollPosition.value = position
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(
        _aggressiveCreature,
        _error,
        _isLoading
    ) { aggressiveCreature, error, isLoading ->
        MobListUiState(
            aggressiveCreatures = aggressiveCreature,
            error = error,
            isLoading = isLoading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MobListUiState()
    )


    init {
        lauch()
    }

    internal fun lauch() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                creatureuseCases.getAggressiveCreatures().collect {
                    creatures -> _aggressiveCreature.value = creatures

                }
            } catch (e: Exception) {
                when (e) {
                    is CreatureFetchException -> Log.e(
                        "CreatureFetchException MobListScreenViewModel",
                        "${e.message}"
                    )

                    is CreaturesFetchLocalException -> Log.e(
                        "CreaturesFetchLocalException MobListScreenViewModel",
                        "${e.message}"
                    )

                    else -> Log.e(
                        "Unexpected Exception occurred MobListScreenViewModel",
                        "${e.message}"
                    )
                }
            }
        }
    }
}