package com.rabbitv.valheimviki.domain.use_cases.biome.get_or_fetch_biomes

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetOrFetchBiomesUseCase @Inject constructor(private val biomeRepository: BiomeRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<Biome>>  {

        return biomeRepository.getLocalBiomes().map { biomes -> biomes.sortedBy { it.order } }
    }
}