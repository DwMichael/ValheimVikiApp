package com.rabbitv.valheimviki.domain.model.ore_deposit

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity(tableName = "ore_deposits")
@Serializable
data class OreDeposit(
    @PrimaryKey(autoGenerate = false)
    override val id: String,
    override val category: String,
    override val name: String,
    val description: String,
    val order: Int,
    override val imageUrl: String
) : ItemData
