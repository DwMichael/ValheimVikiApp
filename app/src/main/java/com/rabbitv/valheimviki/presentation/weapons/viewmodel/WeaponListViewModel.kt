@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.weapons.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state.UiCategoryChipState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeaponListViewModel @Inject constructor(
    private val weaponRepository: WeaponUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow(WeaponSubCategory.MELEE_WEAPON)
    private val _selectedChip = MutableStateFlow<WeaponSubType?>(null)

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
        }.flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<UiCategoryChipState<WeaponSubCategory, WeaponSubType?, Weapon>> =
        combine(
            _weapons,
            _selectedCategory,
            _selectedChip,
            connectivityObserver.isConnected.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = true
            )
        ) { weaponList, category, chip, isConnected ->
            if (isConnected) {
                UiCategoryChipState.Success(category, chip, weaponList)
            } else {
                if (weaponList.isNotEmpty()) {
                    UiCategoryChipState.Success(category, chip, weaponList)
                } else {
                    UiCategoryChipState.Error(
                        category,
                        chip,
                        "No internet connection and no local data available for the selected filters."
                    )
                }
            }
        }.onStart {
            emit(UiCategoryChipState.Loading(_selectedCategory.value, _selectedChip.value))
        }.catch { e ->
            Log.e("WeaponListVM", "Error in uiState flow", e)
            emit(
                UiCategoryChipState.Error(
                    _selectedCategory.value,
                    _selectedChip.value,
                    e.message ?: "An unknown error occurred"
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = UiCategoryChipState.Loading(_selectedCategory.value, _selectedChip.value)
        )


    fun onCategorySelected(cat: WeaponSubCategory) {
        _selectedCategory.value = cat
    }

    fun onChipSelected(chip: WeaponSubType?) {
        _selectedChip.value = chip
    }
}