package com.rabbitv.valheimviki.domain.use_cases.creatures.get_mini_bosses

//class GetMiniBossesUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
//    @OptIn(ExperimentalCoroutinesApi::class)
//    operator fun invoke(language: String): Flow<List<CreatureDtoX>> {
//        return creatureRepository.getMiniBosses()
//            .flatMapConcat { localCreatures ->
//                if (localCreatures.isEmpty()) {
//                    try {
//                        val response = creatureRepository.fetchCreatures(language)
//                        creatureRepository.storeCreatures(response.creatures)
//                        creatureRepository.getAllCreatures()
//                    } catch (e: Exception) {
//                        throw FetchException("No local data available and failed to fetch from internet.")
//                    }
//                } else {
//                    flowOf(localCreatures)
//                }
//            }
//            .map { creatureList ->
//                creatureList.sortedWith(
//                    compareBy<CreatureDtoX> { creature ->
//                        TYPE_ORDER_MAP.getOrElse(creature.typeName) { Int.MAX_VALUE }
//                    }.thenBy { it.order }
//                )
//            }
//    }
//
//
//}