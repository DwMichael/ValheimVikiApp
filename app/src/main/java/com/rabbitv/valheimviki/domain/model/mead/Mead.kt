package com.rabbitv.valheimviki.domain.model.mead

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity("meads")
@Serializable
data class Mead(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imageUrl: String,
    val category: String,
    val subCategory: String,
    val name: String,
    val description: String? = null,
    @SerializedName("recipe_output") val recipeOutput: String? = null,
    @SerializedName("effect") val effect: String? = null,
    @SerializedName("duration") val duration: String? = null,
    @SerializedName("cooldown") val cooldown: String? = null,
    val order: Int,
)