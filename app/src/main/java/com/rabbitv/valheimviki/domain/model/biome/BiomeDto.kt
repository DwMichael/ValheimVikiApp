package com.rabbitv.valheimviki.domain.model.biome


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ErrorResponseDto
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class BiomeDto(
    @SerializedName("success")
    override val success: Boolean,
    @SerializedName("message")
    override val error: String,
    @SerializedName("error")
    override val errorDetails: String,
    @SerializedName("biomes")
    val biomes: List<BiomeDtoX>
) : ErrorResponseDto
