package com.example.domain.entities.tool

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.model.tool.pickaxe.Pickaxe
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("tools")
@Serializable
data class Tool(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    override val imageUrl: String,
    override val category: String,
    val subCategory: String,
    override val name: String,
    val description: String,
    @SerializedName("UpgradeInfo") val upgradeInfoList: List<Pickaxe>? = emptyList(),
    val order: Int,
) : ItemData
