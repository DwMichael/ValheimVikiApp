package com.rabbitv.valheimviki.domain.use_cases.biome

import com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes.GetAllBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes.RefetchBiomes

data class BiomeUseCases(
    val getAllBiomesUseCase: GetAllBiomesUseCase,
    val refetchBiomes: RefetchBiomes,
)