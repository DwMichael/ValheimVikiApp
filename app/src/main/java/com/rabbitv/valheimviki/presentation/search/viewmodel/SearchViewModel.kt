package com.rabbitv.valheimviki.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.domain.use_cases.search.SearchUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val searchUseCases: SearchUseCases
) : ViewModel() {


	val uiState = searchUseCases.getAllSearchObjectsUseCase()
		.map { searchResults ->
			UiState.Success(searchResults)
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = UiState.Loading()
		)
}