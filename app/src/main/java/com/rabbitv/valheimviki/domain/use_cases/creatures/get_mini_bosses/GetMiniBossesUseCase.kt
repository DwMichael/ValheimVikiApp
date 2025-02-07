package com.rabbitv.valheimviki.domain.use_cases.creatures.get_mini_bosses

import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.creature.Type
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMiniBossesUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    suspend operator fun invoke(language: String): Flow<List<CreatureDtoX>> {
        val response = creatureRepository.fetchCreatures(language)

        creatureRepository.storeCreatures(response.creatures)

        return creatureRepository.getMiniBosses()
            .map { creatureList ->
                creatureList.sortedWith(
                    compareBy<CreatureDtoX> { creature ->
                        typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                    }
                        .thenBy { it.order }
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