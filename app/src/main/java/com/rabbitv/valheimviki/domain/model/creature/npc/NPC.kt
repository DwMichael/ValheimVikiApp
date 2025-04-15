package com.rabbitv.valheimviki.domain.model.creature.npc

import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Serializable
data class NPC(
    override val id: String,
    val category: String,
    val subCategory: String,
    override val imageUrl: String,
    override val name: String,
    val description: String,
    val order: Int,
): ItemData