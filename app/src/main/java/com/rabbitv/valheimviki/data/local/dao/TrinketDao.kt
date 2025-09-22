package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import kotlinx.coroutines.flow.Flow

@Dao
interface TrinketDao {

	@Query("SELECT * FROM trinkets")
	fun getLocalTrinkets(): Flow<List<Trinket>>

	@Query("SELECT * FROM trinkets WHERE id = :id")
	fun getTrinketById(id: String): Flow<Trinket?>

	@Query("SELECT * FROM trinkets WHERE id IN (:ids)")
	fun getTrinketsByIds(ids: List<String>): Flow<List<Trinket>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertTrinkets(trinkets: List<Trinket>)
}