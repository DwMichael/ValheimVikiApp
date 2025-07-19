package com.rabbitv.valheimviki.presentation.creatures.mob_list.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.presentation.creatures.mob_list.model.MobListUiState
import com.rabbitv.valheimviki.presentation.creatures.mob_list.model.MobUiEvent
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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


@HiltViewModel
class MobListViewModel @Inject constructor(
	private val creatureUseCases: CreatureUseCases,
	private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
	private val _selectedSubCategory = MutableStateFlow(CreatureSubCategory.PASSIVE_CREATURE)


	@OptIn(ExperimentalCoroutinesApi::class)
	private val _creaturesBySelectedSubCat: Flow<List<Creature>> =
		_selectedSubCategory
			.flatMapLatest { subCat ->
				creatureUseCases.getCreaturesBySubCategory(subCat)
					.catch { e ->
						Log.e("MobListScreenVM", "getCreaturesBySubCategory failed", e)
						emit(emptyList())
					}
			}


	val mobUiState: StateFlow<MobListUiState> = combine(
		_creaturesBySelectedSubCat,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = true
		),
		_selectedSubCategory
	) { creatures, isConnected, selectedSubCategory ->
		when {
			creatures.isNotEmpty() -> MobListUiState(
				selectedSubCategory = _selectedSubCategory.value,
				listState = UIState.Success(creatures)
			)

			isConnected -> MobListUiState(
				selectedSubCategory = _selectedSubCategory.value,
				listState = UIState.Loading
			)

			else -> MobListUiState(
				selectedSubCategory = _selectedSubCategory.value,
				listState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}.onCompletion { error -> println("Error -> ${error?.message}") }.catch { e ->
		Log.e("MobListScreenVM", "Error in creatureUiState flow", e)
		emit(
			MobListUiState(
				selectedSubCategory = _selectedSubCategory.value,
				listState = UIState.Error(e.message ?: "An unknown error occurred")
			)
		)

	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(3000),
		initialValue = MobListUiState(
			selectedSubCategory = CreatureSubCategory.PASSIVE_CREATURE,
			listState = UIState.Loading
		)
	)

	fun onEvent(event: MobUiEvent) {
		when (event) {
			is MobUiEvent.CategorySelected -> {
				_selectedSubCategory.update { event.category }
			}
		}
	}

}