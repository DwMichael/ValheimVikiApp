package com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes

import android.util.Log
import com.rabbitv.valheimviki.domain.exceptions.BiomeFetchException
import com.rabbitv.valheimviki.domain.exceptions.BiomesInsertException
import com.rabbitv.valheimviki.domain.exceptions.RelationFetchAndInsertException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class RefetchBiomesUseCase @Inject constructor(
    private val biomeRepository: BiomeRepository,
    private val repository: RelationsRepository
) {

    suspend operator fun invoke(language: String): Flow<List<Biome>> = coroutineScope {

        try {
            val response: Response<List<Biome>> = biomeRepository.fetchBiomes(language)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val deferred = async(Dispatchers.IO) {
                    try {
                        biomeRepository.storeBiomes(responseBody)
                    } catch (e: Exception) {
                        throw BiomesInsertException("Insert Biomes failed: ${e.message}")
                    }
                }
                launch(Dispatchers.IO) {
                    try {
                        repository.fetchAndInsertRelations()
                    } catch (e: Exception) {
                        Log.e("RelationsError", "Failed to fetch relations", e)
                        throw RelationFetchAndInsertException("Fetching relations and Inserting failed: ${e.message}")
                    }
                }

                deferred.await()
            }else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "No error body"
                throw BiomeFetchException("API request failed with code $errorCode: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("BiomeError", "Failed to process biomes", e)
            throw BiomeFetchException("Fetching biomes failed: ${e.message}")
        }

        return@coroutineScope biomeRepository.getLocalBiomes()
            .map { biomes ->
                biomes.sortedWith(
                    compareBy { it.order }
                )
            }
    }
}