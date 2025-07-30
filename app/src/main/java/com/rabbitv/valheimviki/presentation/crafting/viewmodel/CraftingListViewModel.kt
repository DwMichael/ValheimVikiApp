package com.rabbitv.valheimviki.presentation.crafting.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.presentation.crafting.model.CraftingListUiEvent
import com.rabbitv.valheimviki.presentation.crafting.model.CraftingListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class CraftingListViewModel @Inject constructor(
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _selectedChip = MutableStateFlow<CraftingSubCategory?>(null)
	internal val filteredCraftingObjectWithSelection: Flow<Pair<List<CraftingObject>, CraftingSubCategory?>> =
		combine(
			craftingObjectUseCases.getLocalCraftingObjectsUseCase(),
			_selectedChip,
		) { allCraftingObjects, chip ->
			if (chip == null) {
				return@combine Pair(allCraftingObjects.sortedBy { it.name }, chip)
			}
			Pair(allCraftingObjects.filter { it.subCategory == chip.toString() }
				.sortedBy { it.order }, chip)
		}.flowOn(defaultDispatcher)


	val uiState: StateFlow<CraftingListUiState> = combine(
		filteredCraftingObjectWithSelection,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = true
		)
	) { (craftingObjects, selectedChip), isConnected ->
		when {
			craftingObjects.isNotEmpty() -> CraftingListUiState(
				selectedChip = selectedChip,
				craftingListUiState = UIState.Success(craftingObjects),

				)

			isConnected -> CraftingListUiState(
				selectedChip = selectedChip,
				craftingListUiState = UIState.Loading
			)

			else -> CraftingListUiState(
				selectedChip = selectedChip,
				craftingListUiState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}.catch { e ->
		Log.e("CraftingListVM", "Error in uiState flow", e)
		emit(
			CraftingListUiState(
				selectedChip = _selectedChip.value,
				craftingListUiState = UIState.Error(e.message ?: "An unknown error occurred")
			)
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = CraftingListUiState(
			selectedChip = null,
			craftingListUiState = UIState.Loading
		)
	)

	fun onEvent(event: CraftingListUiEvent) {
		when (event) {
			is CraftingListUiEvent.ChipSelected -> {
				if (_selectedChip.value == event.chip) {
					_selectedChip.update { null }
				} else {
					_selectedChip.update { event.chip }
				}
			}
		}
	}
}