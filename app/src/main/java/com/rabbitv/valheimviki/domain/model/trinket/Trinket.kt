package com.rabbitv.valheimviki.domain.model.trinket

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("trinkets")
@Serializable
data class Trinket(
	@PrimaryKey
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String?,
	override val name: String,
	override val description: String?,
	@SerializedName("Effects")
	val effects: List<String> = emptyList(),
	@SerializedName("Adrenaline")
	val adrenaline: Int? = null,
	@SerializedName("Duration")
	val duration: String? = null,
	val order: Int,
) : ItemData
