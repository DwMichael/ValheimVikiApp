package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.mead.Mead
import kotlinx.coroutines.flow.Flow


@Dao
interface MeadDao {

	@Query("SELECT * FROM meads")
	fun getLocalMeads(): Flow<List<Mead>>

	@Query("SELECT * FROM meads where subCategory = :subCategory")
	fun getMeadsBySubCategory(subCategory: String): Flow<List<Mead>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertMeads(meads: List<Mead>)

}
