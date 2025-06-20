package com.rabbitv.valheimviki.domain.model.armor

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UpgradeArmorInfo(
    @SerializedName("Armor") val armor: Int? = null,
    @SerializedName("Quality") val upgradeLevel: Int? = null,
    @SerializedName("Durability") val durability: Int? = null,
    @SerializedName("Station_level") val stationLevel: Int? = null,
)
