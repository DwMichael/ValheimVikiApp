package com.rabbitv.valheimviki.domain.model.material

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity(tableName = "materials")
@Serializable
data class Material(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String,
	override val name: String,
	override val description: String? = null,
	@SerializedName("usage") val usage: String? = null,
	@SerializedName("growth_time") val growthTime: String? = null,
	@SerializedName("NeedCultivatorGround") val needCultivatorGround: String? = null,
	@SerializedName("price") val price: Int? = null,
	@SerializedName("effect") val effect: String? = null,
	@SerializedName("sell_price") val sellPrice: Int? = null,
	val order: Int,
	val subType: String? = null
) : ItemData
