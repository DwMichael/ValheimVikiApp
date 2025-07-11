package com.rabbitv.valheimviki.domain.model.food

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity(tableName = "food")
@Serializable
data class Food(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String,
	override val name: String,
	override val description: String,
	val order: Int,
	@SerializedName("Eitr") val eitr: Int? = null,
	@SerializedName("Health") val health: Int? = null,
	@SerializedName("Weight") val weight: Double? = null,
	@SerializedName("Healing") val healing: Int? = null,
	@SerializedName("Stamina") val stamina: Int? = null,
	@SerializedName("Duration") val duration: String? = null,
	@SerializedName("Fork_Type") val forkType: String? = null,
	@SerializedName("Stack_size") val stackSize: Int? = null,
) : ItemData

