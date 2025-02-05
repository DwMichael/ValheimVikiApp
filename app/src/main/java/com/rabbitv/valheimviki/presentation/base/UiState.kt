package com.rabbitv.valheimviki.presentation.base

data class UiState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)