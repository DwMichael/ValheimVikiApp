package com.rabbitv.valheimviki.data.repository.biome

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.domain.exceptions.NetworkExceptionHandler
import com.rabbitv.valheimviki.domain.model.api_response.ApiResponse
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BiomeRepositoryImpl @Inject constructor(
    private val apiService: ApiBiomeService,
    private val biomeDao: BiomeDao,
) : BiomeRepository {

    override fun getLocalBiomes(): Flow<List<Biome>> {
        return biomeDao.getAllBiomes()
    }

    override fun getBiomeById(biomeId: String): Biome {
        return biomeDao.getBiomeById(biomeId)
    }

    override suspend fun fetchBiomes(lang: String): ApiResponse<Biome> {
        try {
            val response = apiService.fetchBiomes(lang)


            return if (response.success) {
                ApiResponse(
                    success = true,
                    message = "OK",
                    error = null,
                    data = response.data,
                )
            } else {
                ApiResponse(
                    success = false,
                    error = response.error,
                    message = response.message,
                    data = emptyList(),
                )
            }

        } catch (e: Exception) {
            val networkException = NetworkExceptionHandler.handleException(e)
            return ApiResponse(
                data = emptyList(),
                message = networkException.message,
                success = networkException.success,
                error = networkException.error
            )
        }
    }

    override suspend fun storeBiomes(biomes: List<Biome>) {

        if (biomes.isNotEmpty()) {
            biomeDao.insertAllBiomes(biomes)
        }
    }
}