package com.rabbitv.valheimviki.domain.model.creature


import androidx.annotation.Keep
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ErrorResponseDto
import kotlinx.serialization.Serializable

@Keep
@Entity(tableName = "creatures")
@Serializable
data class CreatureDto(
    @SerializedName("success")
    override val success: Boolean,
    @SerializedName("message")
    override val error: String,
    @SerializedName("error")
    override val errorDetails: String,
    @SerializedName("creatures")
    val creatures: List<CreatureDtoX>
) : ErrorResponseDto