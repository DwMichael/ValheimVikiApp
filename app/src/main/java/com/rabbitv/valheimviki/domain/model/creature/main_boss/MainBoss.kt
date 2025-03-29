package com.rabbitv.valheimviki.domain.model.creature.main_boss

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity(tableName = "bosses")
@Serializable
data class MainBoss(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("creatureId")
    override val id: String,
    @SerializedName("biomeId")
    val biomeId: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("imageUrl")
    override val imageUrl: String,
    @SerializedName("name")
    override val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("levels")
    val levels: Int,
    @SerializedName("Base HP")
    val baseHP: Long,
    @SerializedName("Weakness")
    val weakness: String,
    @SerializedName("Resistance")
    val resistance: String,
    @SerializedName("Base Damage")
    val baseDamage: String,
    @SerializedName("CollapseImmune")
    val collapseImmune: String,
    @SerializedName("Forsaken Power")
    val forsakenPower: String,
): ItemData
