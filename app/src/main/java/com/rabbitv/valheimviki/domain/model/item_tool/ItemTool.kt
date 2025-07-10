package com.rabbitv.valheimviki.domain.model.item_tool

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.model.item_tool.tool_upgrade_info.ToolsUpgradeInfo
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("tools")
@Serializable
data class ItemTool(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String,
	override val name: String,
	override val description: String,
	@SerializedName("HowToUse") val howToUse: String? = null,
	@SerializedName("GeneralInfo") val generalInfo: String? = null,
	@SerializedName("UpgradeInfo") val upgradeInfoList: List<ToolsUpgradeInfo>? = emptyList(),
	val order: Int,
) : ItemData

