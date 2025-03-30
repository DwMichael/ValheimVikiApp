package com.rabbitv.valheimviki.domain.model.api_response

import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ErrorResponseDto


data class ApiResponse<T>(
    @SerializedName("success")
    override val success: Boolean,
    @SerializedName("message")
    override val message: String?,
    @SerializedName("error")
    override val error: String?,
    @SerializedName("data")
    val data: List<T>
) : ErrorResponseDto


data class ApiResponseSecond<T>(@SerializedName("data") val data: List<T>)