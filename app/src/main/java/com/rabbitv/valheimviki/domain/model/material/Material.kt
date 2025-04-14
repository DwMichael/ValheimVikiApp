package com.rabbitv.valheimviki.domain.model.material

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable
@Entity(tableName = "materials")
@Serializable
data class Material(
    @PrimaryKey (autoGenerate = false)
    override val id: String,
    override val imageUrl: String,
    val category: String,
    val subCategory: String,
    override val name: String,
    val description: String? = null,
    @SerialName("usage") val usage: String? = null,
    @SerialName("growth_time") val growthTime: String? = null,
    @SerialName("NeedCultivatorGround") val needCultivatorGround: String? = null,
    val order: Int,
    val subType: String? = null
):ItemData
