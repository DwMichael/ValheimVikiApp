package com.rabbitv.valheimviki.domain.model.item_tool.pickaxe

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Pickaxe(
	@SerializedName("Pierce") val pierceDamage: Int?,
	@SerializedName("Pickaxe") val pickaxeDamage: Int?,
	@SerializedName("Quality") val quality: Int?,
	@SerializedName("Durability") val durability: Int?,
	@SerializedName("Station_level") val stationLevel: Int?,
)