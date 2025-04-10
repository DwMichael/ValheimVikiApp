package com.rabbitv.valheimviki.domain.use_cases.creatures

import com.rabbitv.valheimviki.domain.use_cases.creatures.fetchCreaturesAndInsert.FetchCreaturesAndInsertUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_aggressive_creatures.GetAggressiveCreatures
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_creature_by_id.GetCreatureByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_creature_by_id_and_subcategory.GetCreatureByIdAndSubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_creatures_by_ids.GetCreaturesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_bosses.GetMainBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_mini_bosses.GetMiniBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_npcs.GetNPCsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_passive_creatures.GetPassiveCreature
import com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures.RefetchCreaturesUseCase

data class CreatureUseCases(
    val getCreaturesByIds: GetCreaturesByIdsUseCase,
    val getCreatureById: GetCreatureByIdUseCase,
    val getCreatureByIdAndSubCategoryUseCase: GetCreatureByIdAndSubCategoryUseCase,
    val getMainBossesUseCase: GetMainBossesUseCase,
    val getMiniBossesUseCase: GetMiniBossesUseCase,
    val getAggressiveCreatures: GetAggressiveCreatures,
    val getPassiveCreature: GetPassiveCreature,
    val getNPCsUseCase: GetNPCsUseCase,

    val fetchCreatureAndInsertUseCase: FetchCreaturesAndInsertUseCase,
    val refetchCreaturesUseCase: RefetchCreaturesUseCase,
)