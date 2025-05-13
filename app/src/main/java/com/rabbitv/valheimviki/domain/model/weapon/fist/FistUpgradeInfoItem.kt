package com.example.domain.entities.weapon.metaData.fist

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FistUpgradeInfoItem(
    @SerializedName("Slash") val slashDamage: Int?,
    @SerializedName("Quality") val upgradeLevels: Int?,
    @SerializedName("Durability") val durability: Int?,
    @SerializedName("Station_level") val stationLevel: Int?,
)

