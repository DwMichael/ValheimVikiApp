package com.rabbitv.valheimviki.domain.use_cases.biome.get_biomes_by_ids

import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import javax.inject.Inject

class GetBiomesByIdsUseCase @Inject constructor(
	private val biomeRepository: BiomeRepository
) {
	operator fun invoke(ids: List<String>) = biomeRepository.getBiomesByIds(ids)
}