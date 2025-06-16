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
    @SerializedName("Chop") val chopDamage: Int? = null,
    @SerializedName("Pure") val pureDamage: Int? = null,
    @SerializedName("Pickaxe") val pickaxeDamage: Int? = null,
    @SerializedName("Damage_Absorbed_Blood Magic_0") val damageAbsorbedBloodMagic0: Int? = null,
    @SerializedName("Maximum Skeletons Controllable") val maximumSkeletonsControllable: Int? = null,
    @SerializedName("Damage_Absorbed_Blood_Magic_100") val damageAbsorbedBloodMagic100: Int? = null,
    @SerializedName("Chop_trees") val chopTreesDamage: Double? = null,
)
