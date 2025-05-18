package com.rabbitv.valheimviki.domain.model.armor

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Entity("armors")
@Serializable
data class Armor(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    override val imageUrl: String,
    override val category: String,
    val subCategory: String,
    override val name: String,
    val description: String?,
    @SerialName("UpgradeInfo") val upgradeInfoList: List<UpgradeArmorInfo>? = emptyList(),
    val order: Int
) : ItemData