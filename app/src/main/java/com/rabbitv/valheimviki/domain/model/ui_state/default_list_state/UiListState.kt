package com.rabbitv.valheimviki.domain.model.ui_state.default_list_state

enum class ErrorType {
    SERVER_DOWN,
    UNKNOWN_ERROR,
    INTERNET_CONNECTION,
}

sealed class UiListState<out T> {
    data object Loading : UiListState<Nothing>()
    data class Success<out T>(val list: List<T>) : UiListState<T>()
    data class Error(
        val message: String,
        val errorType: ErrorType = ErrorType.INTERNET_CONNECTION,
    ) : UiListState<Nothing>()
}