package com.rabbitv.valheimviki.presentation.creatures.mob_list.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.presentation.creatures.mob_list.model.MobListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MobListViewModel @Inject constructor(
    private val creatureCases: CreatureUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    private val _creatureList = MutableStateFlow<List<Creature>>(emptyList())
    private val _selectedSubCategory =
        MutableStateFlow<CreatureSubCategory>(CreatureSubCategory.PASSIVE_CREATURE)


    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    

    val uiState = combine(
        _creatureList,
        _selectedSubCategory,
        _isConnection,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        MobListUiState(
            creatureList = values[0] as List<Creature>,
            selectedSubCategory = values[1] as CreatureSubCategory,
            isConnection = values[2] as Boolean,
            isLoading = values[3] as Boolean,
            error = values[4] as String?,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        MobListUiState()
    )


    init {
        launch()
    }

    internal fun launch(subCategory: CreatureSubCategory = CreatureSubCategory.PASSIVE_CREATURE) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                creatureCases.getCreaturesBySubCategory(subCategory)
                    .collect { creatures ->
                        _creatureList.value = creatures
                    }
                _isLoading.value = false
                _error.value = null
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "somthing goes wrong"
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

    fun onCategorySelected(cat: CreatureSubCategory) {
        _selectedSubCategory.value = cat
        launch(cat)
    }
}