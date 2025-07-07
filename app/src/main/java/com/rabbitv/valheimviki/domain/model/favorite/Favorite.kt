package com.rabbitv.valheimviki.domain.model.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "favorite")
data class Favorite(
	@PrimaryKey @Serializable val id: Int,
	@Serializable val itemId: String,
	@Serializable val category: String,
	@Serializable val subCategory: String,
)