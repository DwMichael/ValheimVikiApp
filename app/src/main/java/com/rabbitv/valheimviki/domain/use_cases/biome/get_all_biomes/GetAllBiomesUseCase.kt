package com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.utils.Constants.STAGE_ORDER_MAP
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetAllBiomesUseCase @Inject constructor(private val biomeRepository: BiomeRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<BiomeDtoX>> {
        return biomeRepository.getAllBiomes()
            .flatMapConcat { localBiomes ->
                if (localBiomes.isEmpty()) {
                    try {
                        val response = biomeRepository.fetchBiomes(language)
                        biomeRepository.storeBiomes(response.biomes)
                        biomeRepository.getAllBiomes()
                    } catch (e: Exception) {
                        throw FetchException("No local data available and failed to fetch from internet.")
                    }
                } else {
                    flowOf(localBiomes)
                }
            }
            .map { biomes ->
                biomes.sortedWith(
                    compareBy(
                        { STAGE_ORDER_MAP[it.stage] ?: Int.MAX_VALUE },
                        { it.order }
                    )
                )
            }
    }
}