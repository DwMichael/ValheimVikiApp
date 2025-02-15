package com.rabbitv.valheimviki.domain.use_cases.creatures.get_bosses

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.utils.Constants.TYPE_ORDER_MAP
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetBossesUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<CreatureDtoX>> {
        return creatureRepository.getMainBosses()
            .flatMapConcat { localCreatures ->
                if (localCreatures.isEmpty()) {
                    try {
                        val response = creatureRepository.fetchCreatures(language)
                        creatureRepository.storeCreatures(response.creatures)
                        creatureRepository.getMainBosses()
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
                        TYPE_ORDER_MAP.getOrElse(creature.typeName) { Int.MAX_VALUE }
                    }.thenBy { it.order }
                )
            }
    }
}