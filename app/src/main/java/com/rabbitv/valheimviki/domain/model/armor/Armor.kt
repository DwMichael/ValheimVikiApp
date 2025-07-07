package com.rabbitv.valheimviki.domain.model.armor

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable


@Entity("armors")
@Serializable
data class Armor(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String,
	override val name: String,
	val description: String?,
	@SerializedName("UpgradeInfo") val upgradeInfoList: List<UpgradeArmorInfo>? = emptyList(),
	@SerializedName("Effects")
	val effects: String? = null,
	@SerializedName("usage")
	val usage: String? = null,
	@SerializedName("Full_Set_Effects")
	val fullSetEffects: String? = null,
	val order: Int
) : ItemData