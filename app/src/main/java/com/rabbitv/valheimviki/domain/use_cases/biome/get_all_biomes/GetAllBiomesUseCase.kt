package com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetAllBiomesUseCase @Inject constructor(private val biomeRepository: BiomeRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<Biome>>  {

        return biomeRepository.getLocalBiomes()
            .flatMapConcat { localBiomes ->
                if (localBiomes.isNotEmpty()) {
                    flowOf(localBiomes)
                } else {
                    try {
                        val response = biomeRepository.fetchBiomes(language)
                        biomeRepository.storeBiomes(response.data)
                        biomeRepository.getLocalBiomes()
                    } catch (e: Exception) {
                        throw FetchException("No local data available and failed to fetch from internet.")
                    }
                }
            }.map { biomes -> biomes.sortedBy { it.order } }
    }
}