package com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes

import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.biome.Stage
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RefetchBiomes @Inject constructor(private val biomeRepository: BiomeRepository) {

    suspend operator fun invoke(language: String): Flow<List<BiomeDtoX>> {
        val response = biomeRepository.fetchBiomes(language)

        biomeRepository.storeBiomes(response.biomes)

        return biomeRepository.getAllBiomes()
            .map { biomes ->
                biomes.sortedWith(
                    compareBy(
                        { stageOrderMap[it.stage] ?: Int.MAX_VALUE },
                        { it.order }
                    )
                )
            }
    }

    private val stageOrderMap = mapOf(
        Stage.EARLY.toString() to 1,
        Stage.MID.toString() to 2,
        Stage.LATE.toString() to 3
    )

}