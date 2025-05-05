package com.rabbitv.valheimviki.domain.model.creature.aggresive

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LevelCreatureData(
    @SerializedName("level") val level: Int,
    @SerializedName("Base HP") val baseHp: Int,
    @SerializedName("Base Damage") val baseDamage: String? = null,
    @SerializedName("image") val image: String? = null
)
