package com.rabbitv.valheimviki.domain.use_cases.biome.get_biomes_by_ids

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBiomesByIdsUseCase @Inject constructor(
	private val biomeRepository: BiomeRepository
) {
	operator fun invoke(ids: List<String>): Flow<List<Biome>> = biomeRepository.getBiomesByIds(ids)
}