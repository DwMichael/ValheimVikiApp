package com.rabbitv.valheimviki.presentation.creatures.mob_list.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class MobListViewModel @Inject constructor(
    private val creatureUseCases: CreatureUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _selectedSubCategory = MutableStateFlow(CreatureSubCategory.PASSIVE_CREATURE)


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _creaturesBySelectedSubcat: Flow<List<Creature>> =
        _selectedSubCategory
            .flatMapLatest { subCat ->
                creatureUseCases.getCreaturesBySubCategory(subCat)
                    .catch { e ->
                        Log.e("MobListScreenVM", "getCreaturesBySubCategory failed", e)
                        emit(emptyList())
                    }
            }


    val mobUiState: StateFlow<UiCategoryState<CreatureSubCategory, Creature>> = combine(
        _creaturesBySelectedSubcat,
        connectivityObserver.isConnected,
        _selectedSubCategory
    ) { creatures, isConnected, selectedSubCategory ->
        if (isConnected) {
            if (creatures.isNotEmpty()) {
                UiCategoryState.Success(selectedSubCategory, creatures)
            } else {
                UiCategoryState.Loading(selectedSubCategory)
            }
        } else {
            if (creatures.isNotEmpty()) {
                UiCategoryState.Success(selectedSubCategory, creatures)
            } else {
                UiCategoryState.Error(
                    selectedSubCategory,
                    "No internet connection and no local data available."
                )
            }
        }
    }.onStart {
        emit(UiCategoryState.Loading(_selectedSubCategory.value))
    }.catch { e ->
        Log.e("MobListScreenVM", "Error in creatureUiState flow", e)
        emit(
            UiCategoryState.Error(
                _selectedSubCategory.value,
                e.message ?: "An unknown error occurred"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000),
        initialValue = UiCategoryState.Loading(_selectedSubCategory.value)
    )


    fun onCategorySelected(cat: CreatureSubCategory) {
        _selectedSubCategory.value = cat
    }
}