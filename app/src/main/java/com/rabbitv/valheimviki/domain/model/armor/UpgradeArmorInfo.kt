package com.rabbitv.valheimviki.domain.model.armor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UpgradeArmorInfo(
    @SerialName("Armor") val armor: Int? = null,
    @SerialName("Effects") val effect: String? = null,
    @SerialName("Quality") val upgradeLevel: Int? = null,
    @SerialName("Durability") val durability: Int? = null,
    @SerialName("Station_level") val stationLevel: Int? = null,
    @SerialName("Price") val price: Int? = null,
    @SerialName("usage") val usage: String? = null
)
