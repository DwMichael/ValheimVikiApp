package com.rabbitv.valheimviki.domain.model.biome


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable


@Entity(tableName = "biomes")
@Serializable
data class Biome(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    @SerializedName("imageUrl")
    override val imageUrl: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("nameContent")
    override val name: String,
    @SerializedName("descriptionContent")
    val description: String,
    @SerializedName("order")
    val order: Int
) : ItemData