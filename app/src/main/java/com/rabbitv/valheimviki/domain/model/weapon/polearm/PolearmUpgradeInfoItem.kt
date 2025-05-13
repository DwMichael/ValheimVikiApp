package com.example.domain.entities.weapon.metaData.polearm

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PolearmUpgradeInfoItem(
    @SerializedName("Pierce") val pierceDamage: Int?,
    @SerializedName("Quality") val upgradeLevels: Int?,
    @SerializedName("Lightning") val lightningDamage: Int?,
    @SerializedName("Durability") val durability: Int?,
    @SerializedName("Station_level") val stationLevel: Int?,
)

