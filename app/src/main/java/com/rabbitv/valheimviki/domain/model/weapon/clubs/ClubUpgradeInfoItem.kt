package com.example.domain.entities.weapon.metaData.clubs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubUpgradeInfoItem(
    @SerialName("Fire") val fireDamage: Int?,
    @SerialName("Blunt") val bluntDamage: Int?,
    @SerialName("Frost") val frostDamage: Int?,
    @SerialName("Pierce") val pierceDamage: Int?,
    @SerialName("Spirit") val spiritDamage: Int?,
    @SerialName("Quality") val upgradeLevels: Int?,
    @SerialName("Durability") val durability: Int?,
    @SerialName("Station_level") val stationLevel: Int?,
)

