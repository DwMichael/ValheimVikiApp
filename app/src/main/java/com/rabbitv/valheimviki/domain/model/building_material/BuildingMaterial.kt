package com.rabbitv.valheimviki.domain.model.building_material

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("building_materials")
@Serializable
data class BuildingMaterial(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String,
	override val name: String,
	val description: String,
	@SerializedName("comfort_level") val comfortLevel: Int? = null,
	val order: Int,
	val subType: String? = null,
) : ItemData
