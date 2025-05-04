package com.rabbitv.valheimviki.domain.model.tree

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity("trees")
@Serializable
data class Tree(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    override val imageUrl: String,
    override val category: String,
    override val name: String,
    val description: String,
    val order: Int,
) : ItemData
