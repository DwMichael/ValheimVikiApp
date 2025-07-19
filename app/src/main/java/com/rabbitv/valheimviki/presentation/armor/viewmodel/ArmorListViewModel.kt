package com.rabbitv.valheimviki.presentation.armor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.presentation.armor.model.ArmorListUiState
import com.rabbitv.valheimviki.presentation.armor.model.ArmorUiEvent
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

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class ArmorListViewModel @Inject constructor(
	private val armorUseCases: ArmorUseCases,
	private val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _selectedChip = MutableStateFlow<ArmorSubCategory?>(null)

	internal val armors: Flow<List<Armor>> = armorUseCases.getLocalArmorsUseCase()
		.combine(_selectedChip) { allArmors, chip ->
			allArmors
				.filter { chip == null || it.subCategory == chip.toString() }
		}.flowOn(defaultDispatcher)
		.onCompletion { error -> println("Error -> ${error?.message}") }
		.catch { println("Caught -> ${it.message}") }


	val uiState: StateFlow<ArmorListUiState> = combine(
		armors,
		_selectedChip,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = false
		)
	) { armors, selectedChip, isConnected ->
		when {
			armors.isNotEmpty() -> ArmorListUiState(
				selectedChip = selectedChip,
				armorsUiState = UIState.Success(armors)
			)

			isConnected -> ArmorListUiState(
				selectedChip = selectedChip,
				armorsUiState = UIState.Loading
			)

			else -> ArmorListUiState(
				selectedChip = selectedChip,
				armorsUiState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}
		.onCompletion { error -> println("Error -> ${error?.message}") }
		.catch { e ->
			Log.e("ArmorListVM", "Error in uiState flow", e)
			emit(
				ArmorListUiState(
					selectedChip = _selectedChip.value,
					armorsUiState = UIState.Error(e.message ?: "An unknown error occurred")
				)
			)
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = ArmorListUiState(
				selectedChip = null,
				armorsUiState = UIState.Loading
			)
		)


	fun onEvent(event: ArmorUiEvent) {
		when (event) {
			is ArmorUiEvent.ChipSelected -> {
				_selectedChip.update { event.chip }
			}
		}
	}


}