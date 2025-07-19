package com.rabbitv.valheimviki.presentation.creatures.mini_bosses.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MiniBossesGridViewModel @Inject constructor(
	val creatureUseCases: CreatureUseCases,
	val connectivityObserver: NetworkConnectivity,
) : ViewModel() {

	val miniBossUiListState: StateFlow<UIState<List<MiniBoss>>> = combine(
		creatureUseCases.getMiniBossesUseCase()
			.catch { e ->
				Log.e("MiniBossScreenVM", "getLocalCreaturesUseCase failed in combine", e)
				emit(emptyList())
			},
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = true
		)
	) { creatures, isConnected ->
		when {
			creatures.isNotEmpty() -> UIState.Success(creatures)
			isConnected -> UIState.Loading
			else -> UIState.Error(error_no_connection_with_empty_list_message.toString())
		}
	}.onCompletion { error -> println("Error -> ${error?.message}") }.catch { e ->
		Log.e("MiniBossScreenVM", "Error in creatureUiState flow", e)
		emit(UIState.Error(e.message ?: "An unknown error occurred"))
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(3000),
		initialValue = UIState.Loading
	)


}