package com.rabbitv.valheimviki.domain.use_cases.biome

import com.rabbitv.valheimviki.domain.use_cases.biome.get_local_biomes.GetLocalBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase

data class BiomeUseCases(
    val getBiomeByIdUseCase: GetBiomeByIdUseCase,
    val getLocalBiomesUseCase: GetLocalBiomesUseCase,
)