package com.rabbitv.valheimviki.domain.model.armor

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Entity("armors")
@Serializable
data class Armor(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imageUrl: String,
    val category: String,
    val subCategory: String,
    val name: String,
    val description: String?,
    @SerialName("UpgradeInfo") val upgradeInfoList: List<UpgradeArmorInfo>? = emptyList(),
    val order: Int
)