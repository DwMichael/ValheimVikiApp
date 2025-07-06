package com.rabbitv.valheimviki.domain.model.crafting_object

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("crafting_objects")
@Serializable
data class CraftingObject(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String,
	override val name: String,
	val description: String,
	val order: Int,
) : ItemData
