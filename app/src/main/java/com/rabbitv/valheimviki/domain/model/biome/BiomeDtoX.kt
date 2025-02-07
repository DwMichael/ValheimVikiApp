package com.rabbitv.valheimviki.domain.model.biome


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Keep
@Entity(tableName = "biomes")
@Serializable
data class BiomeDtoX(
    @SerializedName("biomeId")
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    @SerializedName("stage")
    val stage: String,
    @SerializedName("imageUrl")
    override val imageUrl: String,
    @SerializedName("nameContent")
    override val name: String,
    @SerializedName("descriptionContent")
    val description: String,
    @SerializedName("order")
    val order: Int
) : ItemData