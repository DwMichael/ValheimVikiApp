package com.example.domain.entities.weapon.metaData.blood_magic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BloodMagicUpgradeInfoItem(
    @SerialName("Chop") val chopDamage: Int?,
    @SerialName("Fire") val fireDamage: Int?,
    @SerialName("Pure") val pureDamage: Int?,
    @SerialName("Pickaxe") val pickaxeDamage: Int?,
    @SerialName("Quality") val upgradeLevels: Int?,
    @SerialName("Durability") val durability: Int?,
    @SerialName("Station_level") val stationLevel: Int?,
    @SerialName("Damage_Absorbed_Blood Magic_0") val damageAbsorbedBloodMagic0: Int?,
    @SerialName("Maximum Skeletons Controllable") val maximumSkeletonsControllable: Int?,
    @SerialName("Damage_Absorbed_Blood_Magic_100") val damageAbsorbedBloodMagic100: Int?,
)

