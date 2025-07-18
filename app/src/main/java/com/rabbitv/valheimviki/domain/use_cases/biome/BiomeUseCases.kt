package com.rabbitv.valheimviki.domain.use_cases.biome

import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biomes_by_ids.GetBiomesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_local_biomes.GetLocalBiomesUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class BiomeUseCases @Inject constructor(
	val getBiomeByIdUseCase: GetBiomeByIdUseCase,
	val getBiomesByIdsUseCase: GetBiomesByIdsUseCase,
	val getLocalBiomesUseCase: GetLocalBiomesUseCase,
)