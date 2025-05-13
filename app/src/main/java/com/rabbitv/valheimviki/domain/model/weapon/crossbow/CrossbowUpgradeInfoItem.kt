package com.example.domain.entities.weapon.metaData.crossbow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrossbowUpgradeInfoItem(
    @SerialName("Pierce") val pierceDamage: Int?,
    @SerialName("Quality") val upgradeLevels: Int?,
    @SerialName("Durability") val durability: Int?,
    @SerialName("Station_level") val stationLevel: Int?,
)
