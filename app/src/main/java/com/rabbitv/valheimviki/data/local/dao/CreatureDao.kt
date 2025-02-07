package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatureDao {
    @Query("SELECT * FROM creatures")
    fun getAllCreatures(): Flow<List<CreatureDtoX>>

    @Query("SELECT * FROM creatures WHERE type = 'BOSS'")
    fun getMainBosses(): Flow<List<CreatureDtoX>>


    @Query("SELECT * FROM creatures WHERE type = 'MINI_BOSS'")
    fun getMiniBosses(): Flow<List<CreatureDtoX>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCreatures(creatures: List<CreatureDtoX>)


}