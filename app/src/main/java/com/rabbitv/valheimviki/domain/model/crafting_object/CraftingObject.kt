package com.rabbitv.valheimviki.domain.model.crafting_object

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity("crafting_objects")
@Serializable
data class CraftingObject(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imageUrl: String,
    val category: String,
    val subCategory: String,
    val name: String,
    val description: String,
    val order: Int,
)
