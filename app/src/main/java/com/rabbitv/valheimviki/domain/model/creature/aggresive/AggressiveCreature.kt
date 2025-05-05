package com.rabbitv.valheimviki.domain.model.creature.aggresive

import kotlinx.serialization.Serializable

@Serializable
data class AggressiveCreature(
    val id: String,
    val category: String,
    val subCategory: String,
    val imageUrl: String,
    val name: String,
    val description: String,
    val order: Int,
    val weakness: String?,
    val resistance: String?,
    val baseDamage: String,
    val levels: List<LevelCreatureData>,
    val abilities: String?,
)