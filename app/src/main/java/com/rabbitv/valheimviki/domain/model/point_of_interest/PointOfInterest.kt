package com.rabbitv.valheimviki.domain.model.point_of_interest

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("point_of_interest")
@Serializable
data class PointOfInterest(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String,
	override val name: String,
	override val description: String,
	val order: Int,
) : ItemData


