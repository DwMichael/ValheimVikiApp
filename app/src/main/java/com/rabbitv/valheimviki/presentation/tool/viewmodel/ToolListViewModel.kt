package com.rabbitv.valheimviki.presentation.tool.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.presentation.tool.model.ToolListUiEvent
import com.rabbitv.valheimviki.presentation.tool.model.ToolListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class ToolListViewModel @Inject constructor(
	private val toolUseCases: ToolUseCases,
	private val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _selectedChip =
		MutableStateFlow<ToolSubCategory?>(null)

	private val itemTool: Flow<Pair<List<ItemTool>, ToolSubCategory?>> =
		combine(
			toolUseCases.getLocalToolsUseCase(),
			_selectedChip,
		) { allTools, category ->
			if (category == null) {
				return@combine Pair(allTools, category)
			}

			Pair(
				allTools.filter { it.subCategory == category.name },
				category
			)
		}.flowOn(defaultDispatcher)


	val uiState: StateFlow<ToolListUiState> = combine(
		itemTool,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = false
		)
	) { (tools, selectedChip), isConnected ->
		when {
			tools.isNotEmpty() -> {
				ToolListUiState(
					selectedCategory = selectedChip,
					toolState = UIState.Success(tools.sortedBy { it.order })
				)
			}

			isConnected -> ToolListUiState(
				selectedCategory = selectedChip,
				toolState = UIState.Loading
			)

			else -> ToolListUiState(
				selectedCategory = selectedChip,
				toolState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}.catch { e ->
		Log.e("ToolListVM", "Error in uiState flow", e)
		emit(
			ToolListUiState(
				selectedCategory = _selectedChip.value,
				toolState = UIState.Error(e.message ?: "An unknown error occurred")
			)
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = ToolListUiState(
			selectedCategory = null,
			toolState = UIState.Loading
		)
	)

	fun onEvent(event: ToolListUiEvent) {
		when (event) {
			is ToolListUiEvent.CategorySelected -> {
				if (_selectedChip.value == event.category) {
					_selectedChip.update { null }
				} else {
					_selectedChip.update { event.category }
				}
			}
		}
	}


}