package com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures

import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.model.creature.Type
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class RefetchCreatures @Inject constructor(private val creatureRepository: CreatureRepository) {
    suspend operator fun invoke(language: String, refetchUseCase: RefetchUseCases):
            Flow<List<CreatureDtoX>> {
        val response = creatureRepository.fetchCreatures(language)
        if (response.error != null) {
            throw FetchException(response.error)
        }

        creatureRepository.storeCreatures(response.creatures)

        when (refetchUseCase) {
            RefetchUseCases.GET_ALL_CREATURES -> return creatureRepository.getAllCreatures()
                .map { creatureList ->
                    creatureList.sortedWith(
                        compareBy<CreatureDtoX> { creature ->
                            typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                        }.thenBy { it.order }
                    )
                }

            RefetchUseCases.GET_BOSSES -> return creatureRepository.getMainBosses()
                .map { creatureList ->
                    creatureList.sortedWith(
                        compareBy<CreatureDtoX> { creature ->
                            typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                        }.thenBy { it.order }
                    )
                }

            RefetchUseCases.GET_MINI_BOSSES -> return creatureRepository.getMiniBosses()
                .map { creatureList ->
                    creatureList.sortedWith(
                        compareBy<CreatureDtoX> { creature ->
                            typeOrderMap.getOrElse(creature.typeName) { Int.MAX_VALUE }
                        }.thenBy { it.order }
                    )
                }
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