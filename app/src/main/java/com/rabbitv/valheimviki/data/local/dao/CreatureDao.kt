package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.creature.Creature
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatureDao {
    @Query("SELECT * FROM creatures WHERE category='CREATURE' ")
    fun getLocalCreatures(): Flow<List<Creature>>

    @Query("SELECT * FROM creatures WHERE category='CREATURE' AND subCategory = :subCategory ")
    fun getCreaturesBySubCategory(subCategory:String): Flow<List<Creature>>

    @Query("SELECT * FROM creatures WHERE category='CREATURE' AND subCategory = :subCategory AND id = :creatureId")
    fun getCreatureByIdAndSubCategory(creatureId: String, subCategory:String): Creature

    @Query("SELECT * FROM creatures WHERE category='CREATURE' AND id = :creatureId")
    fun getCreatureById(creatureId: String): Creature?

    @Query("SELECT * FROM creatures WHERE category='CREATURE' AND subCategory != 'BOSS' AND id IN (:ids)")
    fun getCreaturesByIds(ids: List<String>): List<Creature>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreatures(creatures: List<Creature>)
}