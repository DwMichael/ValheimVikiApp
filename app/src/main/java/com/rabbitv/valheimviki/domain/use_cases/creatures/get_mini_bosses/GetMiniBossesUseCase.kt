package com.rabbitv.valheimviki.domain.use_cases.creatures.get_mini_bosses

import com.rabbitv.valheimviki.data.mappers.toMiniBosses
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetMiniBossesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<MiniBoss>> {
        val creatureType = CreatureType.MINI_BOSS
        return creatureRepository.getCreaturesBySubCategory(creatureType.toString())
            .flatMapConcat {localMainBoss ->
                if(localMainBoss.isNotEmpty())
                {
                    flowOf(localMainBoss)
                }else
                {
                    try {
                        withContext(Dispatchers.IO) {
                            creatureRepository.fetchCreatureAndInsert(language)
                        }
                        creatureRepository.getCreaturesBySubCategory(creatureType.toString())
                    } catch (e: Exception)
                    {
                        throw FetchException("No local data available and failed to fetch from internet.")
                    }
                }
            }.map { mainBosses -> mainBosses.toMiniBosses().sortedBy { it.order } }
    }
}