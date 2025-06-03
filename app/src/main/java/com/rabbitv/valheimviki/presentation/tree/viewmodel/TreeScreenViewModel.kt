package com.rabbitv.valheimviki.presentation.tree.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.ErrorType
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.UiListState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TreeUIState(
    val trees: List<Tree> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class TreeScreenViewModel @Inject constructor(
    private val treesUseCases: TreeUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {


    val uiState: StateFlow<UiListState<Tree>> = combine(
        treesUseCases.getLocalTreesUseCase(),
        connectivityObserver.isConnected.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    ) { oreDeposits, isConnected ->
        if (isConnected) {
            if (oreDeposits.isNotEmpty()) {
                UiListState.Success(oreDeposits)
            } else {
                UiListState.Loading
            }
        } else {
            if (oreDeposits.isNotEmpty()) {
                UiListState.Success(oreDeposits)
            } else {
                UiListState.Error(
                    "No internet connection and no local data available. Try to connect to the internet again.",
                    ErrorType.INTERNET_CONNECTION
                )
            }
        }
    }.onStart {
        emit(UiListState.Loading)
    }.catch { e ->
        Log.e("TreesListVM", "Error in uiState flow", e)
        emit(UiListState.Error(e.message ?: "An unknown error occurred"))
    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        UiListState.Loading
    )
}