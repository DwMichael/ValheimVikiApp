package com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetBiomeByIdUseCase @Inject constructor(private val biomeRepository: BiomeRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(biomeId: String): Flow<Biome?> {
        return biomeRepository.getBiomeById(biomeId).flowOn(Dispatchers.IO)
    }
}