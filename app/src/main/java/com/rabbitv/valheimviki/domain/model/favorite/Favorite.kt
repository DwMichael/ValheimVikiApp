package com.rabbitv.valheimviki.domain.model.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity(tableName = "favorite")
data class Favorite(
	@PrimaryKey
	@Serializable override val id: String,
	@Serializable override val name: String,
	@Serializable override val imageUrl: String,
	@Serializable override val category: String,
	@Serializable override val subCategory: String?
) : ItemData