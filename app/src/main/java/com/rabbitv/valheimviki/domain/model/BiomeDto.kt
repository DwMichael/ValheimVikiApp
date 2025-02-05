package com.rabbitv.valheimviki.domain.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class BiomeDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("biomes")
    val biomes: List<BiomeDtoX>
)