package com.rabbitv.valheimviki.presentation.mead.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.presentation.mead.model.MeadUiEvent
import com.rabbitv.valheimviki.presentation.mead.model.MeadUiSate
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class MeadListViewModel @Inject constructor(
	private val meadUseCases: MeadUseCases,
	private val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _selectedSubCategory =
		MutableStateFlow<MeadSubCategory>(MeadSubCategory.MEAD_BASE)

	val uiState: StateFlow<MeadUiSate> = combine(
		meadUseCases.getLocalMeadsUseCase(),
		_selectedSubCategory,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = false
		)
	) { meadList, selectedSubCategory, isConnected ->

		val filteredMeadList = meadList
			.filter { it.subCategory == selectedSubCategory.toString() }
			.sortedBy { it.order }
		
		when {
			filteredMeadList.isNotEmpty() -> {
				MeadUiSate(
					selectedCategory = selectedSubCategory,
					meadState = UIState.Success(filteredMeadList)
				)
			}

			isConnected -> MeadUiSate(
				selectedCategory = selectedSubCategory,
				meadState = UIState.Loading
			)

			else -> MeadUiSate(
				selectedCategory = selectedSubCategory,
				meadState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}.flowOn(defaultDispatcher)
		.catch { e ->
			Log.e("MeadListVM", "Error in uiState flow", e)
			emit(
				MeadUiSate(
					selectedCategory = _selectedSubCategory.value,
					meadState = UIState.Error(e.message ?: "An unknown error occurred")
				)
			)
		}.stateIn(
			viewModelScope,
			SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = MeadUiSate(
				selectedCategory = MeadSubCategory.MEAD_BASE,
				meadState = UIState.Loading
			)
		)

	fun onEvent(event: MeadUiEvent) {
		when (event) {
			is MeadUiEvent.CategorySelected -> {
				_selectedSubCategory.update { event.category }
			}
		}
	}

}