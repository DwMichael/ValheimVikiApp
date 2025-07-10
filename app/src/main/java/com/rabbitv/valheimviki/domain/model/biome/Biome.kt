package com.rabbitv.valheimviki.domain.model.biome


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable


@Entity(tableName = "biomes")
@Serializable
data class Biome(
	@SerializedName("id")
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	@SerializedName("category")
	override val category: String,
	override val subCategory: String? = null,
	@SerializedName("imageUrl")
	override val imageUrl: String,
	@SerializedName("name")
	override val name: String,
	@SerializedName("description")
	override val description: String,
	@SerializedName("order")
	val order: Int,
) : ItemData