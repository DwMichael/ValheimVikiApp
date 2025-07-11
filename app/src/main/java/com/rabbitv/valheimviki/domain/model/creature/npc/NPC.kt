package com.rabbitv.valheimviki.domain.model.creature.npc

import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Serializable
data class NPC(
	override val id: String,
	override val category: String,
	override val subCategory: String,
	override val imageUrl: String,
	override val name: String,
	override val description: String,
	val biography: String,
	val location: String,
	val order: Int,
) : ItemData
