package com.rabbitv.valheimviki.presentation.trinkets.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.trinket.TrinketUseCases
import com.rabbitv.valheimviki.presentation.trinkets.model.TrinketUiEvent
import com.rabbitv.valheimviki.presentation.trinkets.model.TrinketUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TrinketListViewModel @Inject constructor(
	private val trinketsUseCase: TrinketUseCases,
	private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {


	val uiState: StateFlow<TrinketUiState> = trinketsUseCase.getLocalTrinketsUseCase().combine(
		connectivityObserver.isConnected
			.stateIn(
				viewModelScope,
				SharingStarted.WhileSubscribed(5000),
				true
			)
	) { trinkets, isConnected ->
		when {
			trinkets.isNotEmpty() -> TrinketUiState(
				trinketsUiState = UIState.Success(trinkets.sortedBy { it.order }),

				)

			isConnected -> TrinketUiState(
				trinketsUiState = UIState.Loading
			)

			else -> TrinketUiState(
				trinketsUiState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}.catch { e ->
		Log.e("TrinketsListVM", "Error in uiState flow", e)
		emit(
			TrinketUiState(
				trinketsUiState = UIState.Error(e.message ?: "An unknown error occurred")
			)
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = TrinketUiState(
			trinketsUiState = UIState.Loading
		)
	)

	fun onEvent(event: TrinketUiEvent) {
	}

}