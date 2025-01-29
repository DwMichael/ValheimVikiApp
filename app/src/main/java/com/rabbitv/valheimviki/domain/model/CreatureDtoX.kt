package com.rabbitv.valheimviki.domain.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Keep
@Entity(tableName = "creatures")
@Serializable
data class CreatureDtoX(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("creatureId")
    val creatureId: String,
    @SerializedName("biomeId")
    val biomeId: String,
    @SerializedName("typeName")
    val typeName: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("primarySpawn")
    val primarySpawn: String?,
    @SerializedName("summoningItems")
    val summoningItems: String?,
    @SerializedName("baseHP")
    val baseHP: Int,
    @SerializedName("baseDamage")
    val baseDamage: String?,
    @SerializedName("forsakenPower")
    val forsakenPower: String?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("weakness")
    val weakness: String?,
    @SerializedName("resistance")
    val resistance: String?,
    @SerializedName("immune")
    val immune: String?,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("order")
    val order: Int
)