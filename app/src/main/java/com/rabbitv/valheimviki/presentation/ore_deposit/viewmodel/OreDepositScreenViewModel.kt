package com.rabbitv.valheimviki.presentation.ore_deposit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class OreDepositScreenViewModel @Inject constructor(
	val oreDepositUseCases: OreDepositUseCases,
	val connectivityObserver: NetworkConnectivity,
) : ViewModel() {

	val uiState: StateFlow<UIState<List<OreDeposit>>> = combine(
		oreDepositUseCases.getLocalOreDepositsUseCase(),
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = false
		)
	) { oreDeposits, isConnected ->
		when {
			oreDeposits.isNotEmpty() -> UIState.Success(oreDeposits)
			isConnected -> UIState.Loading
			else -> UIState.Error(error_no_connection_with_empty_list_message.toString())
		}
	}.onCompletion { error -> println("Error -> ${error?.message}") }
		.catch { e ->
			val errorMessage = when (e) {
				is IOException -> "Problem accessing local data."
				else -> "An unexpected error occurred."
			}
			emit(UIState.Error(errorMessage))
			Log.e("OreDepositScreenVM", "Error in oreUiState flow", e)
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = UIState.Loading
		)


}