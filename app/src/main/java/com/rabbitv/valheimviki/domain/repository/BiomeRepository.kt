package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.biome.Biome
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface BiomeRepository {
    fun getLocalBiomes(): Flow<List<Biome>>
    suspend fun fetchBiomes(lang: String): Response<List<Biome>>
    fun getBiomeById(biomeId: String): Flow<Biome?>
    suspend fun storeBiomes(biomes: List<Biome>)
}
