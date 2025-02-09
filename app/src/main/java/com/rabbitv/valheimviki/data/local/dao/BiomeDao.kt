package com.rabbitv.valheimviki.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import kotlinx.coroutines.flow.Flow

@Dao
interface BiomeDao {
    @Query("SELECT * FROM biomes")
    fun getAllBiomes(): Flow<List<BiomeDtoX>>

    @Query("SELECT * FROM biomes WHERE id = :biomeId")
    fun getBiomeById(biomeId: String): Flow<BiomeDtoX>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBiomes(biomes: List<BiomeDtoX>)
}