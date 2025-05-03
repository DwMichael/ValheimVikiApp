package com.rabbitv.valheimviki.presentation.creatures.mob_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
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
    private val creatureCases: CreatureUseCases
) : ViewModel() {

    private val _creatureList = MutableStateFlow<List<Creature>>(emptyList())
    private val _selectedSubCategory = MutableStateFlow<Int>(0)


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
        _creatureList,
        _selectedSubCategory,
        _error,
        _isLoading
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        MobListUiState(
            creatureList = values[0] as List<Creature>,
            selectedSubCategory = values[1] as Int,
            error = values[2] as String?,
            isLoading = values[3] as Boolean
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
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

    fun selectCreaturesSubCategory(subCategory: CreatureSubCategory) {
        when (subCategory) {
            CreatureSubCategory.PASSIVE_CREATURE -> {
                _selectedSubCategory.value = 0
                launch(subCategory)
            }

            CreatureSubCategory.AGGRESSIVE_CREATURE -> {
                _selectedSubCategory.value = 1
                launch(subCategory)
            }

            CreatureSubCategory.NPC -> {
                _selectedSubCategory.value = 2
                launch(subCategory)
            }

            CreatureSubCategory.BOSS -> null
            CreatureSubCategory.MINI_BOSS -> null
        }
    }
}