package com.rabbitv.valheimviki.data.remote.exceptions

import com.rabbitv.valheimviki.domain.model.error.ErrorResponseDtoImpl
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

object NetworkExceptionHandler {

    fun handleException(e: Exception): ErrorResponseDtoImpl {
        val errorMessage: String
        val isSuccess: Boolean
        val errorDetails: String

        when (e) {
            is UnknownHostException -> {
                errorMessage = "No internet connection. Please check your internet connection."
                isSuccess = false
                errorDetails = "UnknownHostException: ${e.message}"
            }

            is ConnectException -> {
                errorMessage =
                    "Cannot connect to the server. Please check if the server is available."
                isSuccess = false
                errorDetails = "ConnectException: ${e.message}"
            }

            is HttpException -> {
                errorMessage = "Server error. HTTP status code: ${e.code()}"
                isSuccess = false
                errorDetails = "HttpException: ${
                    e.response()?.raw()?.request?.url
                } - Code: ${e.code()} - Message: ${e.message()}"
            }

            else -> {
                errorMessage = "An unknown error occurred while fetching data."
                isSuccess = false
                errorDetails = "Unknown error: ${e.message}"
            }
        }
        return ErrorResponseDtoImpl(
            error = errorMessage,
            success = isSuccess,
            errorDetails = errorDetails
        )
    }
}
