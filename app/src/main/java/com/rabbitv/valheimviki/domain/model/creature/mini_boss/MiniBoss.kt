package com.rabbitv.valheimviki.domain.model.creature.mini_boss

import com.rabbitv.valheimviki.domain.model.creature.Boss
import kotlinx.serialization.Serializable

@Serializable
data class MiniBoss(
    override val id: String,
    override val category: String,
    override val subCategory: String,
    override val imageUrl: String,
    override val name: String,
    override val description: String,
    override val order: Int,
    override val baseHP: Int?,
    override val weakness: String?,
    override val resistance: String?,
    override val baseDamage: String,
    override val collapseImmune: String?,
): Boss()
