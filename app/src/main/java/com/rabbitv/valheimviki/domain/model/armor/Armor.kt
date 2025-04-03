package com.rabbitv.valheimviki.domain.model.armor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Armor(
    val id: String,
    val imageUrl: String,
    val category: String,
    val subCategory: String,
    val name: String,
    val description: String?,
    @SerialName("UpgradeInfo") val upgradeInfoList: List<UpgradeArmorInfo>?,
    val order: Int
)