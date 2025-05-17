package com.example.domain.entities.weapon.metaData.axe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AxeUpgradeInfoItem(
    @SerialName("Slash") val slashDamage: Int?,
    @SerialName("Poison") val poisonDamage: Int?,
    @SerialName("Spirit") val spiritDamage: Int?,
    @SerialName("Quality") val upgradeLevels: Int?,
    @SerialName("Chop_trees") val chopTreesDamage: Double?,
    @SerialName("Durability") val durability: Int?,
    @SerialName("Station_level") val stationLevel: Int?,
)

