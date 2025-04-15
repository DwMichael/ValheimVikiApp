package com.rabbitv.valheimviki.domain.model.creature


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity(tableName = "creatures")
@Serializable
data class Creature(
    @PrimaryKey (autoGenerate = false)
    override val id: String,
    val category: String? = null,
    val subCategory: String? = null,
    override val imageUrl: String,
    override val name: String,
    val description: String? = null,
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
    ):ItemData

