package com.rabbitv.valheimviki.domain.model.ui_state.ui_state

sealed class UiState<T> {
	class Loading<T> : UiState<T>()
	data class Success<T>(val data: T) : UiState<T>()
	data class Error<T>(val message: String) : UiState<T>()
}


//sealed class UIState<out T> {
//	object Loading : UIState<Nothing>()
//	data class Error(val message: String) : UIState<Nothing>()
//	data class Data<T>(val data: T) : UIState<T>()
//}