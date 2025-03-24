package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.api_response.ApiResponse
import com.rabbitv.valheimviki.domain.model.biome.Biome
import kotlinx.coroutines.flow.Flow

interface BiomeRepository {
    fun getAllBiomes(): Flow<List<Biome>>
    suspend fun fetchBiomes(lang: String): ApiResponse<Biome>
    fun getBiomeById(biomeId: String): Flow<Biome>
    suspend fun storeBiomes(biomes: List<Biome>)
}
