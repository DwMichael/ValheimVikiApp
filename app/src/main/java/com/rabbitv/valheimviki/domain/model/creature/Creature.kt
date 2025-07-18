package com.rabbitv.valheimviki.domain.model.creature


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity(tableName = "creatures")
@Serializable
data class Creature(
	@PrimaryKey(autoGenerate = false)
	override val id: String,
	override val category: String,
	override val subCategory: String? = null,
	override val imageUrl: String,
	override val name: String,
	override val description: String? = null,
	val order: Int,
	// Common stats fields
	val levels: Int? = null,
	@SerializedName("Base HP") val baseHP: Int? = null,
	@SerializedName("Weakness") val weakness: String? = null,
	@SerializedName("Resistance") val resistance: String? = null,
	@SerializedName("Base Damage") val baseDamage: String? = null,
	// Boss/MiniBoss specific fields
	@SerializedName("CollapseImmune") val collapseImmune: String? = null,
	@SerializedName("Forsaken Power") val forsakenPower: String? = null,
	// AggressiveCreature/PassiveCreature specific fields
	@SerializedName("Levels") val levelCreatureData: List<LevelCreatureData> = emptyList(),
	// PassiveCreature specific field
	@SerializedName("Notes") val notes: String? = null,
	@SerializedName("Abilities") val abilities: String? = null,
	//NPC
	@SerializedName("Biography") val biography: String? = null,
	@SerializedName("Location") val location: String? = null,
) : ItemData

