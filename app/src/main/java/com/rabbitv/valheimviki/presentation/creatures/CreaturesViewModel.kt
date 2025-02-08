package com.rabbitv.valheimviki.presentation.creatures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreaturesUIState(
    val creatures: List<CreatureDtoX> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)


@HiltViewModel
class CreaturesViewModel @Inject constructor(
    private val creatureUseCases: CreatureUseCases
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _creatureUIState = MutableStateFlow(CreaturesUIState())
    val creatureUIState: StateFlow<CreaturesUIState> = _creatureUIState


    init {
        load()
    }

    private fun load() {
        _creatureUIState.value = _creatureUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                creatureUseCases.getAllCreaturesUseCase("en").collect { sortedCreatures ->
                    _creatureUIState.update { current ->
                        current.copy(
                            creatures = sortedCreatures,
                            isLoading = false,
                        )
                    }
                }

            } catch (e: FetchException) {
                _creatureUIState.value =
                    _creatureUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _creatureUIState.value =
                    _creatureUIState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }

    fun refetchBiomes() {
        _creatureUIState.value = _creatureUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                creatureUseCases.refetchCreatures("en", RefetchUseCases.GET_BOSSES)
                    .collect { sortedMiniBosses ->
                        _creatureUIState.update { current ->
                            current.copy(creatures = sortedMiniBosses, isLoading = false)
                        }
                    }
            } catch (e: FetchException) {
                _creatureUIState.value =
                    _creatureUIState.value.copy(isLoading = false, error = e.message)
            } catch (e: Exception) {
                _creatureUIState.value =
                    _creatureUIState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }

}