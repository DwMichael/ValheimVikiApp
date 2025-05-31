package com.rabbitv.valheimviki.domain.model.ui_state.default_detail_state

sealed class UiDetailState<out T> {
    data object Loading : UiDetailState<Nothing>()
    data class Success<T>(val data: T) : UiDetailState<T>()
    data class Error(val message: String) : UiDetailState<Nothing>()
}