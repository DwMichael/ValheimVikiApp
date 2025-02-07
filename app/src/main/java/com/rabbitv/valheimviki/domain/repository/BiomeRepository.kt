package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.biome.BiomeDto
import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import kotlinx.coroutines.flow.Flow

interface BiomeRepository {
    fun getAllBiomes(): Flow<List<BiomeDtoX>>
    suspend fun fetchBiomes(lang: String): BiomeDto
    fun getBiomeById(biomeId: String): Flow<BiomeDtoX>
}
