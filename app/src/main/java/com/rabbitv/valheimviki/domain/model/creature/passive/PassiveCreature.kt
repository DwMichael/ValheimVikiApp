package com.rabbitv.valheimviki.domain.model.creature.passive

import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Serializable
data class PassiveCreature (
    override val id: String,
    val category: String,
    val subCategory: String,
    override val imageUrl: String,
    override val name: String,
    val description: String,
    val order: Int,
    val levels: Int?,
    val baseHP: Int?,
    val abilities: String?,
    val imageStarOne: String,
    val imageStarTwo: String,
): ItemData