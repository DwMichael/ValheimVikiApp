package com.rabbitv.valheimviki.domain.repository

interface ErrorResponseDto {
    val error: String
    val success: Boolean
    val errorDetails: String
}