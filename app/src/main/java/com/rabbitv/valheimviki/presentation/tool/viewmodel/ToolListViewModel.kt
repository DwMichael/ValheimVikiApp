package com.rabbitv.valheimviki.presentation.tool.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.item_tool.GameTool
import com.rabbitv.valheimviki.domain.model.item_tool.ToolSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ToolListViewModel @Inject constructor(
	private val toolUseCases: ToolUseCases,
	private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {

	private val _selectedChip =
		MutableStateFlow<ToolSubCategory?>(null)

	private val _gameTool: StateFlow<List<GameTool>> =
		combine(
			toolUseCases.getLocalToolsUseCase(),
			_selectedChip,
		) { allTools, category ->
			if (category == null) allTools
			else allTools.filter { it.subCategory == category.name }
		}
			.flowOn(Dispatchers.Default)
			.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), emptyList())


	val uiState: StateFlow<UiCategoryState<ToolSubCategory?, GameTool>> = combine(
		_gameTool,
		_selectedChip,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = false
		)
	) { armors, selectedChip, isConnected ->
		if (isConnected) {
			if (armors.isNotEmpty()) {
				UiCategoryState.Success(selectedChip, armors)
			} else {
				UiCategoryState.Loading(selectedChip)
			}
		} else {
			if (armors.isNotEmpty()) {
				UiCategoryState.Success(selectedChip, armors)
			} else {
				UiCategoryState.Error(
					selectedChip,
					"No internet connection and no local data available. Try to connect to the internet again.",
				)
			}
		}
	}.onStart {
		emit(UiCategoryState.Loading(_selectedChip.value))
	}.catch { e ->
		Log.e("ToolListVM", "Error in uiState flow", e)
		emit(
			UiCategoryState.Error(
				_selectedChip.value,
				e.message ?: "An unknown error occurred"
			)
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = UiCategoryState.Loading(_selectedChip.value)
	)

	fun onChipSelected(cat: ToolSubCategory?) {
		_selectedChip.value = cat
	}
}