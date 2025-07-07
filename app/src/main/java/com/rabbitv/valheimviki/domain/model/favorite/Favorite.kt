package com.rabbitv.valheimviki.domain.model.favorite

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity("favorite")
data class Favorite(
	@Serializable val id: Int,
	@Serializable val itemId: String,
	@Serializable val category: String,
	@Serializable val subCategory: String,
)
