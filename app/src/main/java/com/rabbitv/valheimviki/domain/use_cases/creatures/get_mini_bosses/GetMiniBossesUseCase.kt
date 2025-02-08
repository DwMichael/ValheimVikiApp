package com.rabbitv.valheimviki.domain.use_cases.creatures.get_mini_bosses

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.creature.Type
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMiniBossesUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<CreatureDtoX>> {
        return creatureRepository.getMiniBosses()
            .flatMapConcat { localCreatures ->
                if (localCreatures.isEmpty()) {
                    try {
                        val response = creatureRepository.fetchCreatures(language)
                        creatureRepository.storeCreatures(response.creatures)
                        creatureRepository.getAllCreatures()
                    } catch (e: Exception) {
                        throw FetchException("No local data available and failed to fetch from internet.")
                    }
                } else {
                    flowOf(localCreatures)
                }
            }
            .map { creatureList ->
                creatureList.sortedWith(
                    compareBy<CreatureDtoX> { creature ->
                        typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                    }.thenBy { it.order }
                )
            }
    }


    private val typeOrderMap = mapOf(
        Type.BOSS.toString() to 1,
        Type.MINI_BOSS.toString() to 2,
        Type.AGGRESSIVE_CREATURE.toString() to 3,
        Type.PASSIVE_CREATURE.toString() to 4,
        Type.NPC.toString() to 5,
    )
}