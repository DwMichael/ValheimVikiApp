package com.rabbitv.valheimviki.domain.model.ui_state.default_list_state

sealed class UiListState<out T> {
    data object Loading : UiListState<Nothing>()
    data class Success<out T>(val list: List<T>) : UiListState<T>()
    data class Error(val message: String) : UiListState<Nothing>()
}