package com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.biome.BiomeDto
import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.utils.Constants.STAGE_ORDER_MAP
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RefetchBiomesUseCase @Inject constructor(private val biomeRepository: BiomeRepository) {

    suspend operator fun invoke(language: String): Flow<List<BiomeDtoX>> {
        val response: BiomeDto = biomeRepository.fetchBiomes(language)
        if (response.error != null) {
            throw FetchException(response.error)
        }
        
        biomeRepository.storeBiomes(response.biomes)

        return biomeRepository.getAllBiomes()
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