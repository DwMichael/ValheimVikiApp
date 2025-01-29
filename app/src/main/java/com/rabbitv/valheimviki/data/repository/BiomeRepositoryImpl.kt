package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.BiomeRepository
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BiomeRepositoryImpl @Inject constructor(
    private val apiService: ApiBiomeService,
    private val biomeDao: BiomeDao
): BiomeRepository {
    override fun getAllBiomes(lang: String): Flow<List<BiomeDtoX>> {
        return biomeDao.getAllBiomes()
    }

    override suspend fun refreshBiomes() {
        val biomes = apiService.getAllBiomes()
        biomeDao.insertAllBiomes(biomes.biomes)
    }
}