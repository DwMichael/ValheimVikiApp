package com.rabbitv.valheimviki.domain.use_cases.creatures

import com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_boss_by_id.GetMainBossByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_bosses.GetMainBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures.RefetchCreaturesUseCase

data class CreatureUseCases(
//    val getAllCreaturesUseCase: GetAllCreaturesUseCase,
    val getMainBossesUseCase: GetMainBossesUseCase,
    val getMainBossesByIdUseCase: GetMainBossByIdUseCase,
//    val getMiniBossesUseCase: GetMiniBossesUseCase,
    val refetchCreaturesUseCase: RefetchCreaturesUseCase,
)