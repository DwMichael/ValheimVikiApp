package com.rabbitv.valheimviki.data.repository.biome

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class BiomeRepositoryImpl @Inject constructor(
    private val apiService: ApiBiomeService,
    private val biomeDao: BiomeDao,
) : BiomeRepository {

    override fun getLocalBiomes(): Flow<List<Biome>> {
        return biomeDao.getAllBiomes()
    }

    override fun getBiomeById(biomeId: String): Flow<Biome?> {
        return biomeDao.getBiomeById(biomeId)
    }

    override suspend fun fetchBiomes(lang: String): Response<List<Biome>> {
        return apiService.fetchBiomes(lang)
    }

    override suspend fun storeBiomes(biomes: List<Biome>) {
        check(biomes.isNotEmpty()) { "Biome list cannot be empty , cannot insert ${biomes.size} biomes" }
        biomeDao.insertAllBiomes(biomes)
    }
}