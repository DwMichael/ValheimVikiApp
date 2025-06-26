package com.rabbitv.valheimviki.domain.model.item_tool.tool_upgrade_info

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ToolsUpgradeInfo(
	@SerializedName("Pierce") val pierceDamage: Int?,
	@SerializedName("Pickaxe") val pickaxeDamage: Int?,
	@SerializedName("Quality") val quality: Int?,
	@SerializedName("Durability") val durability: Int?,
	@SerializedName("Station_level") val stationLevel: Int?,
)