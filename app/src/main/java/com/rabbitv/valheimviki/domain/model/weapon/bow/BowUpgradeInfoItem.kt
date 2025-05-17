package com.example.domain.entities.weapon.metaData.clubs.bow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BowUpgradeInfoItem(
    @SerialName("Pierce") val pierceDamage: Int?,
    @SerialName("Poison") val poisonDamage: Int?,
    @SerialName("Spirit") val spiritDamage: Int?,
    @SerialName("Quality") val upgradeLevels: Int?,
    @SerialName("Durability") val durability: Int?,
    @SerialName("Station_level") val stationLevel: Int?,
)

