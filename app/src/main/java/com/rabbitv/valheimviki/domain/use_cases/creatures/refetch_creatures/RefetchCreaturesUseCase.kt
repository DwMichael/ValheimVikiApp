package com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures

import com.rabbitv.valheimviki.data.mappers.toCreatures
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class RefetchCreaturesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    suspend operator fun invoke(language: String, refetchUseCase: RefetchUseCases):
            Flow<List<MainBoss>> {
        val mainBossList = creatureRepository.fetchMainBosses(language)

        val creatureList = mainBossList.toCreatures()

        creatureRepository.insertLocalCreatures(creatureList)

        when (refetchUseCase) {
//            RefetchUseCases.GET_ALL_CREATURES -> return creatureRepository.getAllCreatures()
//                .map { creatureList ->
//                    creatureList.sortedWith(
//                        compareBy<CreatureDtoX> { creature ->
//                            TYPE_ORDER_MAP.getOrElse(creature.typeName) { Int.MAX_VALUE }
//                        }.thenBy { it.order }
//                    )
//                }
            RefetchUseCases.GET_BOSSES -> return creatureRepository.getLocalMainBosses()
                .map { mainBossList -> mainBossList.sortedBy { it.order } }

            RefetchUseCases.GET_ALL_CREATURES -> TODO()
            RefetchUseCases.GET_MINI_BOSSES -> TODO()
        }
    }
}