package com.example.domain.entities.weapon.metaData.sword

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SwordUpgradeInfoItem(
    @SerializedName("Quality") val upgradeLevels: Int?,
    @SerializedName("Fire") val fireDamage: Int?,
    @SerializedName("Frost") val frostDamage: Int?,
    @SerializedName("Slash") val slashDamage: Int?,
    @SerializedName("Spirit") val spiritDamage: Int?,
    @SerializedName("Durability") val durability: Int?,
    @SerializedName("Station_level") val stationLevel: Int?,
)

