package com.rabbitv.valheimviki.domain.model.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "search_fts")
@Fts4(contentEntity = Search::class)
data class SearchFTS(
	@ColumnInfo(name = "rowid")
	@PrimaryKey val rowid: Int,
	val name: String,
//	val description: String,
)
