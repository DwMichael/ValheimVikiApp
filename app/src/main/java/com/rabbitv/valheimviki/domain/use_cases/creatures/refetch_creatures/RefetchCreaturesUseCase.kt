package com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RefetchCreaturesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository,private val repository: RelationsRepository) {
    suspend operator fun invoke(language: String,  refetchUseCase: RefetchUseCases):
            Flow<List<Creature>> {

        withContext(Dispatchers.IO){
            launch {
                repository.fetchAndInsertRelations()
            }
        }

        when (refetchUseCase) {
//            RefetchUseCases.GET_ALL_CREATURES -> return creatureRepository.getAllCreatures()
//                .map { creatureList ->
//                    creatureList.sortedWith(
//                        compareBy<CreatureDtoX> { creature ->
//                            TYPE_ORDER_MAP.getOrElse(creature.typeName) { Int.MAX_VALUE }
//                        }.thenBy { it.order }
//                    )
//                }
            RefetchUseCases.GET_BOSSES -> {
                val creatureList = creatureRepository.fetchCreatureByType(language,CreatureType.BOSS)
                creatureRepository.insertLocalCreatures(creatureList)
                return creatureRepository.getCreaturesBySubCategory(CreatureType.BOSS.toString())
                .map { mainBossList -> mainBossList.sortedBy { it.order } }}

            RefetchUseCases.GET_ALL_CREATURES -> TODO()
            RefetchUseCases.GET_MINI_BOSSES -> {
                val creatureList = creatureRepository.fetchCreatureByType(language,CreatureType.MINI_BOSS)
                println(creatureList)
                creatureRepository.insertLocalCreatures(creatureList)
                return creatureRepository.getCreaturesBySubCategory(CreatureType.MINI_BOSS.toString())
                    .map { mainBossList -> mainBossList.sortedBy { it.order } }}
        }
    }
}