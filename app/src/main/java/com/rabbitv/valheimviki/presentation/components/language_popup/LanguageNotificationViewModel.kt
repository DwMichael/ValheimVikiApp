package com.rabbitv.valheimviki.presentation.components.language_popup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageNotificationViewModel @Inject constructor(
	private val dataStoreUseCases: DataStoreUseCases
) : ViewModel() {
	private val _uiState = MutableStateFlow(LanguageNotificationUiState())
	val uiState: StateFlow<LanguageNotificationUiState> = _uiState.asStateFlow()

	init {
		viewModelScope.launch {
			val alreadyShown = dataStoreUseCases.readLanguagePopupState().first()
			if (!alreadyShown) {
				_uiState.update { it.copy(visible = true) }
				startCountdown()
			}
		}
	}

	fun dismiss() {
		if (!_uiState.value.canDismiss) return
		viewModelScope.launch {
			dataStoreUseCases.saveLanguagePopupState(true)
			_uiState.update { it.copy(visible = false) }
		}
	}

	private suspend fun startCountdown() {
		while (_uiState.value.secondsRemaining > 0) {
			delay(1000)
			_uiState.update { state ->
				state.copy(secondsRemaining = state.secondsRemaining - 1)
			}
		}
	}
}
