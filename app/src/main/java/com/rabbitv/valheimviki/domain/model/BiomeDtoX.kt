package com.rabbitv.valheimviki.domain.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class BiomeDtoX(
    @SerializedName("biomeId")
    @PrimaryKey(autoGenerate = false)
    val biomeId: String,
    @SerializedName("stage")
    val stage: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("nameContent")
    val nameContent: String,
    @SerializedName("descriptionContent")
    val descriptionContent: String,
    @SerializedName("order")
    val order: Int
)