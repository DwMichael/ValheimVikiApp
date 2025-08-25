package com.rabbitv.valheimviki.domain.model.data_refetch_result

sealed class DataRefetchResult {
    object Success : DataRefetchResult()
    data class NetworkError(val message: String) : DataRefetchResult()
    data class Error(val message: String) : DataRefetchResult()
    data class PartialSuccess(
        val successfulCategories: List<String>,
        val failedCategories: Map<String, String>,
        val totalCategories: Int
    ) : DataRefetchResult()
}