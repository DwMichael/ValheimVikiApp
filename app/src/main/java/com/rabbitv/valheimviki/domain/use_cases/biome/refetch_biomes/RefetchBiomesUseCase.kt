package com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.api_response.ApiResponse
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RefetchBiomesUseCase @Inject constructor(private val biomeRepository: BiomeRepository) {

    suspend operator fun invoke(language: String): Flow<List<Biome>> {
        val response: ApiResponse<Biome> = biomeRepository.fetchBiomes(language)
        if (response.error != null) {
            throw FetchException(response.error)
        }

        biomeRepository.storeBiomes(response.data)

        return biomeRepository.getLocalBiomes()
            .map { biomes ->
                biomes.sortedWith(
                    compareBy { it.order }
                )
            }
    }
}