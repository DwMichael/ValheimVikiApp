package com.rabbitv.valheimviki.domain.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rabbitv.valheimviki.domain.repository.ItemData
import kotlinx.serialization.Serializable

@Entity(tableName = "search")
@Serializable
data class Search(
	@PrimaryKey
	@Serializable override val id: String,
	@Serializable override val name: String,
	@Serializable override val description: String?,
	@Serializable override val imageUrl: String,
	@Serializable override val category: String,
	@Serializable override val subCategory: String?
) : ItemData