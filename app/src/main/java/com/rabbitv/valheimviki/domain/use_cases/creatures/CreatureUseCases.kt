package com.rabbitv.valheimviki.domain.use_cases.creatures

import com.rabbitv.valheimviki.domain.use_cases.creatures.get_all_creatures.GetAllCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_bosses.GetBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_mini_bosses.GetMiniBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures.RefetchCreatures

data class CreatureUseCases(
    val getAllCreaturesUseCase: GetAllCreaturesUseCase,
    val getBossesUseCase: GetBossesUseCase,
    val getMiniBossesUseCase: GetMiniBossesUseCase,
    val refetchCreatures: RefetchCreatures,
)