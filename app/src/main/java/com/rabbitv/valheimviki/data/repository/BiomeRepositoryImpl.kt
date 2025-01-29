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

    override suspend fun refreshBiomes(lang:String) {
        val biomes = apiService.getAllBiomes(lang)
        val filteredBiomes = biomes.biomes.filter {
            it.biomeId != "00000000-0000-0000-0000-000000000000"
        }
        biomeDao.insertAllBiomes(filteredBiomes)
    }
}