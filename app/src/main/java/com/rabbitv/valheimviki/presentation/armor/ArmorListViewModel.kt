package com.rabbitv.valheimviki.presentation.armor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArmorListViewModel @Inject constructor(
    private val armorRepository: ArmorRepository,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _selectedChip = MutableStateFlow<ArmorSubCategory?>(null)

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    private val _error: MutableStateFlow<String?> = MutableStateFlow<String?>(null)

    private val _armors: StateFlow<List<Armor>> =
        combine(
            armorRepository.getLocalArmors(),
            _selectedChip,
        ) { allArmors, chip ->
            allArmors
                .filter { chip == null || it.subCategory == chip.toString() }
                .sortedBy { it.order }
        }
            .flowOn(Dispatchers.Default)
            .onStart {
                _isLoading.value = true
                _error.value = null
            }
            .catch { e ->
                Log.e("ArmorListVM", "getLocalArmors failed", e)
                _isLoading.value = false
                _error.value = e.message
                emit(emptyList())
            }
            .onEach {
                _isLoading.value = false
                _error.value = null
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState = combine(
        _armors,
        _selectedChip,
        _isConnection,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        ArmorListUiState(
            armorList = values[0] as List<Armor>,
            selectedChip = values[1] as ArmorSubCategory?,
            isConnection = values[2] as Boolean,
            isLoading = values[3] as Boolean,
            error = values[4] as String?
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArmorListUiState()
    )


    fun onChipSelected(chip: ArmorSubCategory?) {
        _selectedChip.value = chip
    }
}


