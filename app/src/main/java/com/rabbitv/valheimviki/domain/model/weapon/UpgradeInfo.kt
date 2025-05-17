package com.rabbitv.valheimviki.domain.model.weapon

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpgradeInfo(
    @SerializedName("Quality") val upgradeLevels: Int? = null,
    @SerializedName("Fire") val fireDamage: Int? = null,
    @SerializedName("Frost") val frostDamage: Int? = null,
    @SerializedName("Slash") val slashDamage: Int? = null,
    @SerializedName("Spirit") val spiritDamage: Int? = null,
    @SerializedName("Durability") val durability: Int? = null,
    @SerializedName("Station_level") val stationLevel: Int? = null,
    @SerializedName("Lightning") val lightningDamage: Int? = null,
    @SerializedName("Pierce") val pierceDamage: Int? = null,
    @SerializedName("Blunt") val bluntDamage: Int? = null,
    @SerializedName("Poison") val poisonDamage: Int? = null,
    @SerialName("Chop") val chopDamage: Int? = null,
    @SerialName("Pure") val pureDamage: Int? = null,
    @SerialName("Pickaxe") val pickaxeDamage: Int? = null,
    @SerialName("Damage_Absorbed_Blood Magic_0") val damageAbsorbedBloodMagic0: Int? = null,
    @SerialName("Maximum Skeletons Controllable") val maximumSkeletonsControllable: Int? = null,
    @SerialName("Damage_Absorbed_Blood_Magic_100") val damageAbsorbedBloodMagic100: Int? = null,
    @SerialName("Chop_trees") val chopTreesDamage: Double? = null,
)
