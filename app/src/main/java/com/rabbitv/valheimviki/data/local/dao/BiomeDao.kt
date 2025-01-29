package com.rabbitv.valheimviki.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import kotlinx.coroutines.flow.Flow

@Dao
interface BiomeDao {
    @Query("SELECT * FROM biomes")
    fun getAllBiomes(): Flow<List<BiomeDtoX>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBiomes(biomes: List<BiomeDtoX>)
}