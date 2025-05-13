package com.example.domain.entities.weapon.metaData.elemental_magic

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ElementalMagicUpgradeInfoItem(
    @SerializedName("Fire") val fireDamage: Int?,
    @SerializedName("Blunt") val bluntDamage: Int?,
    @SerializedName("Frost") val frostDamage: Int?,
    @SerializedName("Poison") val poisonDamage: Int?,
    @SerializedName("Quality") val upgradeLevels: Int?,
    @SerializedName("Lightning") val lightningDamage: Int?,
    @SerializedName("Durability") val durability: Int?,
    @SerializedName("Station_level") val stationLevel: Int?,
)
