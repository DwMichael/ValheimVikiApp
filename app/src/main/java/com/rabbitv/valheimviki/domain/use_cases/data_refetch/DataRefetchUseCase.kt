package com.rabbitv.valheimviki.domain.use_cases.data_refetch

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.data_refetch_result.DataRefetchResult
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.net.UnknownHostException


class DataRefetchUseCase @Inject constructor(
    private val biomeRepository: BiomeRepository,
    private val creatureRepository: CreaturesRepository,
    private val oreDepositRepository: OreDepositRepository,
    private val relationsRepository: RelationsRepository,
    private val dataStoreUseCases: DataStoreUseCases,
) {
    suspend fun refetchAllData(): DataRefetchResult {
        return try {
            val language = dataStoreUseCases.languageProvider().first()

            if (!shouldRefreshData()) {
                return DataRefetchResult.Success
            }

            val biomeResponse = biomeRepository.fetchBiomes(language)
            val creatureResponse = creatureRepository.fetchCreatures(language)
            val oreDepositResponse = oreDepositRepository.fetchOreDeposits(language)
            val relationResponse = relationsRepository.fetchRelations()

            if (biomeResponse.isSuccessful &&
                creatureResponse.isSuccessful &&
                relationResponse.isSuccessful
            ) {

                biomeResponse.body()?.let { biomes ->
                    if (biomes.isNotEmpty()) {
                        biomeRepository.storeBiomes(biomes)
                    } else {
                        return DataRefetchResult.Error("Empty biome data received")
                    }
                } ?: return DataRefetchResult.Error("Null biome data received")

                creatureResponse.body()?.let { creatures ->
                    if (creatures.isNotEmpty()) {
                        creatureRepository.insertCreatures(creatures)
                    } else {
                        return DataRefetchResult.Error("Empty creature data received")
                    }
                } ?: return DataRefetchResult.Error("Null creature data received")

                oreDepositResponse.body()?.let {
                    if (it.isNotEmpty()) {
                        oreDepositRepository.insertOreDeposit(it)
                    } else {
                        return DataRefetchResult.Error("Empty ore deposit data received")
                    }
                } ?: return DataRefetchResult.Error("Null ore deposit data received")

                relationResponse.body()?.let { relations ->
                    if (relations.isNotEmpty()) {
                        relationsRepository.insertRelations(relations)
                    } else {
                        return DataRefetchResult.Error("Empty relation data received")
                    }
                } ?: return DataRefetchResult.Error("Null relation data received")

                DataRefetchResult.Success
            } else {
                val errorMessage = "API error: " +
                        (biomeResponse.errorBody()?.string() ?: "") +
                        (creatureResponse.errorBody()?.string() ?: "") +
                        (oreDepositResponse.errorBody()?.string() ?: "")
                (relationResponse.errorBody()?.string() ?: "")
                DataRefetchResult.NetworkError(errorMessage)
            }
        } catch (e: Exception) {
            if (e is UnknownHostException
                || e is FetchException
                || e is IOException
            ) {
                DataRefetchResult.NetworkError(e.message ?: "Network error")
            } else {
                DataRefetchResult.Error(e.message ?: "Unknown error")
            }
        }
    }


    private suspend fun shouldRefreshData(): Boolean {
        val hasBiomes = mutableListOf<Biome>()
        val hasCreatures = mutableListOf<Creature>()
        val hasOreDeposits = mutableListOf<OreDeposit>()
        val hasRelations = mutableListOf<Relation>()

        coroutineScope {
            val biome = async {
                biomeRepository.getLocalBiomes().first()
            }
            val creature = async {
                creatureRepository.getLocalCreatures().first()
            }
            val relations = async {
                relationsRepository.getLocalRelations().first()
            }
            val oreDeposit = async {
                oreDepositRepository.getLocalOreDeposits().first()
            }

            hasBiomes.addAll(biome.await())
            hasCreatures.addAll(creature.await())
            hasOreDeposits.addAll(oreDeposit.await())
            hasRelations.addAll(relations.await())
        }

        if ((hasBiomes.isNotEmpty() && hasCreatures.size == 9)
            || (hasCreatures.isNotEmpty() && hasCreatures.size == 83)
            || (hasOreDeposits.isNotEmpty() && hasOreDeposits.size == 9)
            || (hasRelations.isNotEmpty() && hasCreatures.size == 92)
        ) {
            return false
        }

        return true
    }
}