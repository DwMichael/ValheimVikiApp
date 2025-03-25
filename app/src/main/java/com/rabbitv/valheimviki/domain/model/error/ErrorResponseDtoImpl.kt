package com.rabbitv.valheimviki.domain.model.error

import com.rabbitv.valheimviki.domain.repository.ErrorResponseDto

data class ErrorResponseDtoImpl(
    override val success: Boolean,
    override val error: String,
    override val message: String
) : ErrorResponseDto