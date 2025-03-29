package com.rabbitv.valheimviki.domain.use_cases.biome

import com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes.GetAllBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes.RefetchBiomesUseCase

data class BiomeUseCases(
    val getBiomeByIdUseCase: GetBiomeByIdUseCase,
    val getAllBiomesUseCase: GetAllBiomesUseCase,
    val refetchBiomesUseCase: RefetchBiomesUseCase,
)