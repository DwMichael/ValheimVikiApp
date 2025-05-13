package com.example.domain.entities.weapon.metaData.knife

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class KnifeUpgradeInfoItem(
    @SerializedName("Slash") val slashDamage: Int?,
    @SerializedName("Pierce") val pierceDamage: Int?,
    @SerializedName("Spirit") val spiritDamage: Int?,
    @SerializedName("Quality") val upgradeLevels: Int?,
    @SerializedName("Durability") val durability: Int?,
    @SerializedName("Station_level") val stationLevel: Int?,
)

