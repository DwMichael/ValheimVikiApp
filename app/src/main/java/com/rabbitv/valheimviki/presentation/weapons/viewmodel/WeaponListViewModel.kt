package com.rabbitv.valheimviki.presentation.weapons.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.presentation.weapons.model.WeaponListUiState
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
class WeaponListViewModel @Inject constructor(
    private val weaponRepository: WeaponUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = false
    )
    private val _selectedCategory = MutableStateFlow(WeaponSubCategory.MELEE_WEAPON)
    private val _selectedChip = MutableStateFlow<WeaponSubType?>(null)

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    private val _error: MutableStateFlow<String?> = MutableStateFlow<String?>(null)

    private val _weapons: StateFlow<List<Weapon>> =
        combine(
            weaponRepository.getLocalWeaponsUseCase(),
            _selectedCategory,
            _selectedChip,
        ) { allWeapons, category, chip ->
            allWeapons
                .filter { it.subCategory == category.toString() }
                .filter { chip == null || it.subType == chip.toString() }
                .sortedBy { it.order }
        }
            .flowOn(Dispatchers.Default)
            .onStart {
                _isLoading.value = true
                _error.value = null
            }
            .catch { e ->
                Log.e("WeaponListVM", "getLocalWeapons failed", e)
                _isLoading.value = false
                _error.value = e.message
                emit(emptyList())
            }
            .onEach {
                _isLoading.value = false
                _error.value = null
            }
            .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())

    val uiState = combine(
        _weapons,
        _selectedCategory,
        _selectedChip,
        _isConnection,
        _isLoading,
        _error
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        (WeaponListUiState(
            weaponList = values[0] as List<Weapon>,
            selectedCategory = values[1] as WeaponSubCategory,
            selectedChip = values[2] as WeaponSubType?,
            isConnection = values[3] as Boolean,
            isLoading = values[4] as Boolean,
            error = values[5] as String?
        ))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = WeaponListUiState()
    )


    fun onCategorySelected(cat: WeaponSubCategory) {
        _selectedCategory.value = cat
    }

    fun onChipSelected(chip: WeaponSubType?) {
        _selectedChip.value = chip
    }
}