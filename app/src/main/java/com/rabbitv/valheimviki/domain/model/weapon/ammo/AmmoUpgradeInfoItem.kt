package com.example.domain.entities.weapon.metaData.ammo

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AmmoUpgradeInfoItem(
    @SerializedName("Pierce") val pierceDamage: Int?
)
