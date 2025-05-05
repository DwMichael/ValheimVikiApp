package com.rabbitv.valheimviki.domain.model.creature.passive

import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Serializable
data class PassiveCreature(
    override val id: String,
    override val category: String,
    val subCategory: String,
    override val imageUrl: String,
    override val name: String,
    val description: String,
    val notes: String,
    val order: Int,
    val levels: List<LevelCreatureData>,
    val abilities: String?,
    val weaknesses: String?,
) : ItemData