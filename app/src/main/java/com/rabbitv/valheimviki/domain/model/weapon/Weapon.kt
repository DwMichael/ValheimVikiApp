package com.rabbitv.valheimviki.domain.model.weapon

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("weapons")
@Serializable
data class Weapon(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    override val imageUrl: String,
    override val category: String,
    val subCategory: String,
    override val name: String,
    val description: String? = null,
    val order: Int,
    @SerializedName("UpgradeInfo") val upgradeInfoList: List<UpgradeInfo>? = emptyList(),
    val subType: String? = null
) : ItemData

