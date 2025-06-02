package com.rabbitv.valheimviki.presentation.ore_deposit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.OreDepositFetchLocalException
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

data class OreDepositUIState(
    val oreDeposits: List<OreDeposit> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class OreDepositScreenViewModel @Inject constructor(
    private val oreDepositUseCases: OreDepositUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    val isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    private val _oreDepositUIState = MutableStateFlow(OreDepositUIState())
    val oreDepositUIState: StateFlow<OreDepositUIState> = _oreDepositUIState

    init {
        load()
    }

    @VisibleForTesting
    internal fun load() {
        _oreDepositUIState.value = _oreDepositUIState.value.copy(
            isLoading = true,
            error = null,
        )


        viewModelScope.launch(Dispatchers.IO) {
            try {
                oreDepositUseCases.getLocalOreDepositsUseCase().collect { sortedOreDeposits ->
                    _oreDepositUIState.update { current ->
                        current.copy(
                            oreDeposits = sortedOreDeposits,
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is OreDepositFetchLocalException -> Log.e(
                        "OreDepositFetchLocalException OreDepositScreenViewModel",
                        "${e.message}"
                    )

                    else -> Log.e(
                        "Unexpected Exception occurred OreDepositScreenViewModel",
                        "${e.message}"
                    )
                }
            }
        }

    }

}