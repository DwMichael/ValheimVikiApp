package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.domain.exceptions.NetworkExceptionHandler
import com.rabbitv.valheimviki.domain.model.biome.BiomeDto
import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
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

    override suspend fun fetchBiomes(lang: String): BiomeDto {
        try {
            val response = apiService.getAllBiomes(lang)
            val filteredBiomes = response.biomes.filter {
                it.id != "00000000-0000-0000-0000-000000000000"
            }

            return if (response.success) {
                BiomeDto(
                    biomes = filteredBiomes,
                    error = null,
                    success = true,
                    errorDetails = null
                )
            } else {
                BiomeDto(
                    biomes = emptyList(),
                    error = response.error,
                    success = false,
                    errorDetails = response.errorDetails
                )
            }

        } catch (e: Exception) {
            val networkException = NetworkExceptionHandler.handleException(e)
            return BiomeDto(
                biomes = emptyList(),
                error = networkException.error,
                success = networkException.success,
                errorDetails = networkException.errorDetails
            )
        }
    }

    override suspend fun storeBiomes(biomes: List<BiomeDtoX>) {
        if (biomes.isNotEmpty()) {
            biomeDao.insertAllBiomes(biomes)
        }
    }
}