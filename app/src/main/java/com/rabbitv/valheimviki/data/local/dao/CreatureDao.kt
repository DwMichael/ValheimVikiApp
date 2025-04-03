package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatureDao {


    @Query("SELECT * FROM creatures WHERE subCategory = 'BOSS' ")
    fun getLocalMainBosses(): Flow<List<MainBoss>>


    @Query("SELECT * FROM creatures WHERE subCategory = 'BOSS' and id = :id")
    fun getMainBossById(id: String): Flow<MainBoss>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreatures(creatures: List<Creature>)


}