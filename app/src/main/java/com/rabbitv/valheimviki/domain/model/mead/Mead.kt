package com.rabbitv.valheimviki.domain.model.mead

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("meads")
@Serializable
data class Mead(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    override val imageUrl: String,
    override val category: String,
    val subCategory: String,
    override val name: String,
    val description: String? = null,
    @SerializedName("recipe_output") val recipeOutput: String? = null,
    @SerializedName("effect") val effect: String? = null,
    @SerializedName("duration") val duration: String? = null,
    @SerializedName("cooldown") val cooldown: String? = null,
    val order: Int,
) : ItemData