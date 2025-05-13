package com.example.domain.entities.weapon.metaData.spear

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SpearUpgradeInfoItem(
    @SerializedName("Pierce") val pierceDamage: Int?,
    @SerializedName("Quality") val upgradeLevels: Int?,
    @SerializedName("Durability") val durability: Int?,
    @SerializedName("Station_level") val stationLevel: Int?,
)

