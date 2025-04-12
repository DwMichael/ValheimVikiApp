package com.rabbitv.valheimviki.domain.use_cases.biome

import com.rabbitv.valheimviki.domain.use_cases.biome.get_or_fetch_biomes.GetOrFetchBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase

data class BiomeUseCases(
    val getBiomeByIdUseCase: GetBiomeByIdUseCase,
    val getOrFetchBiomesUseCase: GetOrFetchBiomesUseCase,
)