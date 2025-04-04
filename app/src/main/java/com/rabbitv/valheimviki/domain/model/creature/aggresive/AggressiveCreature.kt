package com.rabbitv.valheimviki.domain.model.creature.aggresive

import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Serializable
data class AggressiveCreature(
    override val id: String,
    val category: String,
    val subCategory: String,
    override val imageUrl: String,
    override val name: String,
    val description: String,
    val order: Int,
    val levels: Int?,
    val baseHP: Int?,
    val weakness: String?,
    val resistance: String?,
    val baseDamage: String,
    val imageStarOne: String,
    val imageStarTwo: String,
): ItemData