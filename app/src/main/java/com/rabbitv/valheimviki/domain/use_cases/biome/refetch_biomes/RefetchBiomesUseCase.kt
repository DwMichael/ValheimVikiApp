package com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.api_response.ApiResponse
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RefetchBiomesUseCase @Inject constructor(
    private val biomeRepository: BiomeRepository,
    private val repository: RelationsRepository
) {

    suspend operator fun invoke(language: String): Flow<List<Biome>> = coroutineScope {
        val response: ApiResponse<Biome> = biomeRepository.fetchBiomes(language)
        print(response)
        if (response.error != null) {
            throw FetchException(response.error)
        }

        val deferred = async(Dispatchers.IO) {
            biomeRepository.storeBiomes(response.data)
        }

        launch(Dispatchers.IO) {
            repository.fetchAndInsertRelations()
        }

        deferred.await()

        return@coroutineScope biomeRepository.getLocalBiomes()
            .map { biomes ->
                biomes.sortedWith(
                    compareBy { it.order }
                )
            }
    }
}