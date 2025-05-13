package com.example.domain.entities.weapon.metaData.shield

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ShieldUpgradeInfoItem(
    @SerializedName("Quality") val upgradeLevels: Int?,
    @SerializedName("Durability") val durability: Int?,
    @SerializedName("Block_armor") val spiritDamage: Int?,
    @SerializedName("Station_level") val stationLevel: Int?,
)

