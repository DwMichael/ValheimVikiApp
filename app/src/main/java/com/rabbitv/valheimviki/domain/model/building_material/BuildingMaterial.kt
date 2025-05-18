package com.rabbitv.valheimviki.domain.model.building_material

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity("building_materials")
@Serializable
data class BuildingMaterial(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imageUrl: String,
    val category: String,
    val subCategory: String,
    val name: String,
    val description: String,
    @SerializedName("comfort_level") val comfortLevel: Int? = null,
    val order: Int,
    val subType: String? = null,
)
