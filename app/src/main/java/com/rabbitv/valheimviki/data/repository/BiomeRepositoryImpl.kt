package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.domain.model.BiomeDto
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BiomeRepositoryImpl @Inject constructor(
    private val apiService: ApiBiomeService,
    private val biomeDao: BiomeDao,
) : BiomeRepository {
    override fun getAllBiomes(): Flow<List<BiomeDtoX>> {
        return biomeDao.getAllBiomes()
    }

    override fun getBiomeById(biomeId: String): Flow<BiomeDtoX> {
        return biomeDao.getBiomeById(biomeId)
    }

    //TODO CHANGE NAME OF THIS STUPID FUNCTION
    override suspend fun refreshBiomes(lang: String): BiomeDto {

        try {
            val biomes = apiService.getAllBiomes(lang)
            val filteredBiomes = biomes.biomes.filter {
                it.biomeId != "00000000-0000-0000-0000-000000000000"
            }
            biomeDao.insertAllBiomes(filteredBiomes)
            return biomes
        } catch (e: Exception) {
            return BiomeDto(
                biomes = emptyList(),
                message = e.message ?: "Something goes wrong",
                success = false,
            )
        }
    }


}