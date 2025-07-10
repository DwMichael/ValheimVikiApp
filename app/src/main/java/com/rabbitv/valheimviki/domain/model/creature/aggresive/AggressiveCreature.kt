package com.rabbitv.valheimviki.domain.model.creature.aggresive

import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Serializable
data class AggressiveCreature(
	override val id: String,
	override val category: String,
	override val subCategory: String,
	override val imageUrl: String,
	override val name: String,
	override val description: String,
	val order: Int,
	val weakness: String?,
	val resistance: String?,
	val baseDamage: String,
	val levels: List<LevelCreatureData>,
	val abilities: String?,
) : ItemData