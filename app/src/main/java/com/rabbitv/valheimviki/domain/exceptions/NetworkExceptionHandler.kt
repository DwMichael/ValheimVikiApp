package com.rabbitv.valheimviki.domain.exceptions

import com.rabbitv.valheimviki.domain.model.error.ErrorResponseDtoImpl
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

object NetworkExceptionHandler {

    fun handleException(e: Exception): ErrorResponseDtoImpl {
        val errorMessage: String
        val isSuccess: Boolean
        val error: String

        when (e) {
            is UnknownHostException -> {
                errorMessage = "No internet connection. Please check your internet connection."
                isSuccess = false
                error = "UnknownHostException: ${e.message}"
            }

            is ConnectException -> {
                errorMessage =
                    "Cannot connect to the server. Please check if the server is available."
                isSuccess = false
                error = "ConnectException: ${e.message}"
            }

            is HttpException -> {
                errorMessage = "Server error. HTTP status code: ${e.code()}"
                isSuccess = false
                error = "HttpException: ${
                    e.response()?.raw()?.request?.url
                } - Code: ${e.code()} - Message: ${e.message()}"
            }

            else -> {
                errorMessage = "An unknown error occurred while fetching data."
                isSuccess = false
                error = "Unknown error: ${e.message}"
            }
        }
        
        return ErrorResponseDtoImpl(
            success = isSuccess,
            error = error,
            message = errorMessage

        )
    }
}
