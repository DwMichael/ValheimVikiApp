package com.rabbitv.valheimviki.domain.repository

interface ErrorResponseDto {
    val success: Boolean
    val message: String?
    val error: String?
}