@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.weapons.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.presentation.weapons.model.WeaponUiEvent
import com.rabbitv.valheimviki.presentation.weapons.model.WeaponUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WeaponListViewModel @Inject constructor(
	private val weaponRepository: WeaponUseCases,
	private val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _selectedCategory = MutableStateFlow(WeaponSubCategory.MELEE_WEAPON)
	private val _selectedChip = MutableStateFlow<WeaponSubType?>(null)

	internal val weapons: Flow<List<Weapon>> =
		combine(
			weaponRepository.getLocalWeaponsUseCase(),
			_selectedCategory,
			_selectedChip
		) { all, category, type ->

			if (type == null) {
				return@combine all.filter { it.subCategory == category.toString() }
					.sortedBy { it.order }
			}

			all
				.filter { it.subCategory == category.toString() }
				.filter { it.subType == type.toString() }
				.sortedBy { it.order }
		}.flowOn(defaultDispatcher)
			.onCompletion { error -> println("Error -> ${error?.message}") }
			.catch { println("Caught -> ${it.message}") }


	val uiState: StateFlow<WeaponUiState> =
		combine(
			weapons,
			_selectedCategory,
			_selectedChip,
			connectivityObserver.isConnected.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000),
				initialValue = true
			)
		) { weapons, category, chip, isConnected ->

			when {
				weapons.isNotEmpty() -> WeaponUiState(
					selectedChip = chip,
					selectedCategory = category,
					weaponUiState = UIState.Success(weapons)
				)

				chip != null -> WeaponUiState(
					selectedChip = chip,
					selectedCategory = category,
					weaponUiState = UIState.Success(emptyList())
				)

				isConnected -> WeaponUiState(
					selectedChip = chip,
					selectedCategory = category,
					weaponUiState = UIState.Loading
				)

				else -> WeaponUiState(
					selectedChip = chip,
					selectedCategory = category,
					weaponUiState = UIState.Error(error_no_connection_with_empty_list_message.toString())
				)
			}
		}.onCompletion { error -> println("Error -> ${error?.message}") }
			.catch { e ->
				Log.e("WeaponListVM", "Error in uiState flow", e)
				emit(
					WeaponUiState(
						selectedChip = _selectedChip.value,
						selectedCategory = _selectedCategory.value,
						weaponUiState = UIState.Error(e.message ?: "An unknown error occurred")
					)
				)
			}.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000),
				initialValue = WeaponUiState(
					selectedChip = null,
					selectedCategory = WeaponSubCategory.MELEE_WEAPON,
					weaponUiState = UIState.Loading
				)
			)

	fun onEvent(event: WeaponUiEvent) {
		when (event) {
			is WeaponUiEvent.ChipSelected -> {
				if (_selectedChip.value == event.chip) {
					_selectedChip.update { null }
				} else {
					_selectedChip.update { event.chip }
				}
			}

			is WeaponUiEvent.CategorySelected -> {
				_selectedCategory.update { event.category }
				_selectedChip.update { null }
			}
		}
	}
}