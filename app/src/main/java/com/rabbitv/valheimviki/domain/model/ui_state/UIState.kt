package com.rabbitv.valheimviki.domain.model.ui_state

data class UIState<T>(
    val list: List<T> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val isConnection : Boolean = true
)
