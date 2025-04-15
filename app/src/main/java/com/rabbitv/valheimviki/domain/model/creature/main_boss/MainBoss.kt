package com.rabbitv.valheimviki.domain.model.creature.main_boss

import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Serializable
data class MainBoss(
    override val id: String,
    val category: String,
    val subCategory: String,
    override val imageUrl: String,
    override val name: String,
    val description: String,
    val order: Int,
    val levels: Int,
    val baseHP: Int,
    val weakness: String,
    val resistance: String,
    val baseDamage: String,
    val collapseImmune: String,
    val forsakenPower: String,
): ItemData
