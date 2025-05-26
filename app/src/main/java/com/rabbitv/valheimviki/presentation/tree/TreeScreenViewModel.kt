package com.rabbitv.valheimviki.presentation.tree

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.TreesFetchLocalException
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
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
    val isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    private val _treeUIState = MutableStateFlow(TreeUIState())
    val treeUIState: StateFlow<TreeUIState> = _treeUIState

    init {
        load()
    }

    @VisibleForTesting
    internal fun load() {
        _treeUIState.value = _treeUIState.value.copy(
            isLoading = true,
            error = null,
        )


        viewModelScope.launch(Dispatchers.IO) {
            try {
                treesUseCases.getLocalTreesUseCase().collect { sortedTrees ->
                    _treeUIState.update { current ->
                        current.copy(
                            trees = sortedTrees,
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is TreesFetchLocalException -> Log.e(
                        "TreesFetchLocalException TreeScreenViewModel",
                        "${e.message}"
                    )

                    else -> Log.e(
                        "Unexpected Exception occurred TreeScreenViewModel",
                        "${e.message}"
                    )
                }
            }
        }

    }

}