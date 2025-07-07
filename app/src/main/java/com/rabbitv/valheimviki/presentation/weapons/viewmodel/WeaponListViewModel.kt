@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.weapons.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state.UiCategoryChipState
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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

	private val _weaponState: StateFlow<UiState<List<Weapon>>> =
		weaponRepository.getLocalWeaponsUseCase()
			.map<List<Weapon>, UiState<List<Weapon>>> { weapons -> UiState.Success(weapons) }
			.onStart { emit(UiState.Loading()) }
			.catch { e -> emit(UiState.Error(e.message ?: "An unknown error occurred")) }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000),
				initialValue = UiState.Loading()
			)


	val uiState: StateFlow<UiCategoryChipState<WeaponSubCategory, WeaponSubType?, Weapon>> =
		combine(
			_weaponState,
			_selectedCategory,
			_selectedChip,
			connectivityObserver.isConnected.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000),
				initialValue = true
			)
		) { weaponState, category, chip, isConnected ->

			when (weaponState) {
				is UiState.Loading -> {
					UiCategoryChipState.Loading(category, chip)
				}

				is UiState.Error -> {
					UiCategoryChipState.Error(category, chip, weaponState.message)
				}

				is UiState.Success -> {

					if (weaponState.data.isEmpty() && isConnected) {
						return@combine UiCategoryChipState.Loading(category, chip)
					}

					val filteredList = weaponState.data
						.filter { it.subCategory == category.toString() }
						.filter { chip == null || it.subType == chip.toString() }
						.sortedBy { it.order }

					if (filteredList.isNotEmpty()) {
						UiCategoryChipState.Success(category, chip, filteredList)
					} else {
						if (weaponState.data.isEmpty() && !isConnected) {
							UiCategoryChipState.Error(
								category, chip,
								"No internet connection and no local data available. Try to connect to the internet again.",
							)
						} else {
							UiCategoryChipState.Success(category, chip, emptyList())
						}
					}
				}
			}
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