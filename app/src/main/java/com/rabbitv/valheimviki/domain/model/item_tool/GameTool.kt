package com.rabbitv.valheimviki.domain.model.item_tool

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.model.item_tool.pickaxe.ToolsUpgradeInfo
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("tools")
@Serializable
data class GameTool(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	val subCategory: String,
	override val name: String,
	val description: String,
	@SerializedName("UpgradeInfo") val upgradeInfoList: List<ToolsUpgradeInfo>? = emptyList(),
	val order: Int,
) : ItemData

