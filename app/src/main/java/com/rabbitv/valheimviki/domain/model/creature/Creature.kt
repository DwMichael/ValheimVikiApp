package com.rabbitv.valheimviki.domain.model.creature


import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity(tableName = "creatures")
@Serializable
data class Creature(
    val id: String,
    val category: String? = null,
    val subCategory: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val nameContent: String? = null,
    val description: String? = null,
    val descriptionContent: String? = null,
    val order: Int,
    // Common stats fields
    val levels: Int? = null,
    val baseHP: Int? = null,
    val weakness: String? = null,
    val resistance: String? = null,
    val baseDamage: String? = null,
    // Boss/MiniBoss specific fields
    val collapseImmune: String? = null,
    val forsakenPower: String? = null,
    // AggressiveCreature/PassiveCreature specific fields
    val imageStarOne: String? = null,
    val imageStarTwo: String? = null,
    // PassiveCreature specific field
    val abilities: String? = null,
    )

