package com.rabbitv.valheimviki.domain.use_cases.biome.get_or_fetch_biomes

import com.rabbitv.valheimviki.domain.exceptions.BiomeFetchException
import com.rabbitv.valheimviki.domain.exceptions.BiomesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.BiomesInsertException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetOrFetchBiomesUseCase @Inject constructor(private val biomeRepository: BiomeRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<Biome>>  {

        return biomeRepository.getLocalBiomes()
            .flatMapLatest { localBiomes ->
                if (localBiomes.isNotEmpty()) {
                    flowOf(localBiomes)
                } else {
                    try {
                        val response = biomeRepository.fetchBiomes(language)
                        val responseBody = response.body()
                        if (response.isSuccessful && responseBody?.isNotEmpty() == true){
                            try {
                                biomeRepository.storeBiomes(responseBody)
                            }catch (e: Exception)
                            {
                                throw BiomesInsertException("Insert Biomes failed : ${e.message}")
                            }
                            biomeRepository.getLocalBiomes()
                        }else {
                            val errorCode = response.code()
                            val errorBody = response.errorBody()?.string() ?: "No error body"
                            throw BiomeFetchException("API BIOMES request failed with code $errorCode: $errorBody")
                        }
                    }catch (e: BiomeFetchException) {
                        throw e
                    } catch (e: BiomesInsertException) {
                        throw e
                    } catch (e: Exception) {
                        throw BiomesFetchLocalException("No local data available and failed to fetch from internet.")
                    }
                }
            }.map { biomes -> biomes.sortedBy { it.order } }
    }
}