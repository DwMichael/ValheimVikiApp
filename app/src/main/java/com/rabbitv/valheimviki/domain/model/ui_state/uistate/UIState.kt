package com.rabbitv.valheimviki.domain.model.ui_state.uistate

sealed interface UIState<out T> {
	object Loading : UIState<Nothing>
	data class Error(val message: String) : UIState<Nothing>
	data class Success<T>(val data: T) : UIState<T>
}