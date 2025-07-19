package com.rabbitv.valheimviki.domain.use_cases.biome.get_local_biomes

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetLocalBiomesUseCase @Inject constructor(private val biomeRepository: BiomeRepository) {
	operator fun invoke(): Flow<List<Biome>> {
		return biomeRepository.getLocalBiomes().map { biomes -> biomes.sortedBy { it.order } }
	}
}