package com.rabbitv.valheimviki.domain.model.ui_state

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val list: List<T>) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}