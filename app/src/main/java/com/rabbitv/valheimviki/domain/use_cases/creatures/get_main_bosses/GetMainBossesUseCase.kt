package com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_bosses


import com.rabbitv.valheimviki.data.mappers.toMainBosses
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesInsertException
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GetMainBossesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<MainBoss>> {
        val creatureType = CreatureType.BOSS
        return creatureRepository.getCreaturesBySubCategory(creatureType.toString())
            .flatMapConcat { localMainBoss ->
                if (localMainBoss.isNotEmpty()) {
                    flowOf(localMainBoss)
                } else {
                    try {
                        withContext(Dispatchers.IO) {
                            val response = creatureRepository.fetchCreatures(language)
                            val responseBody = response.body()
                            if (response.isSuccessful && responseBody?.isNotEmpty() == true) {
                                try {
                                    creatureRepository.insertCreatures(responseBody)
                                } catch (e: Exception) {
                                    throw CreaturesInsertException("Insert MainBosses failed : ${e.message}")
                                }
                            } else {
                                val errorCode = response.code()
                                val errorBody = response.errorBody()?.string() ?: "No error body"
                                throw CreaturesFetchLocalException("API MainBosses request failed with code $errorCode: $errorBody")
                            }
                        }
                        creatureRepository.getCreaturesBySubCategory(creatureType.toString())
                    } catch (e: CreatureFetchException) {
                        throw e
                    } catch (e: CreaturesInsertException) {
                        throw e
                    } catch (e: Exception) {
                        throw FetchException("No local data available and failed to fetch from internet.")
                    }
                }
            }.map { mainBosses -> mainBosses.toMainBosses().sortedBy { it.order } }
    }
}