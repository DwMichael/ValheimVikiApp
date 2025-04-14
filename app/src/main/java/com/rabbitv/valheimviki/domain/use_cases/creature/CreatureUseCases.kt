package com.rabbitv.valheimviki.domain.use_cases.creature

import com.rabbitv.valheimviki.domain.use_cases.creature.get_aggressive_creatures.GetAggressiveCreatures
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id.GetCreatureByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id_and_subcategory.GetCreatureByIdAndSubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_relation_and_sub_category.GetCreatureByRelationAndSubCategory
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_ids.GetCreaturesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_main_bosses.GetMainBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_mini_bosses.GetMiniBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_npcs.GetNPCsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_local_creatures.GetLocalCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_passive_creatures.GetPassiveCreature
import com.rabbitv.valheimviki.domain.use_cases.creature.refetch_creatures.RefetchCreaturesUseCase

data class CreatureUseCases(
    val getCreaturesByIds: GetCreaturesByIdsUseCase,
    val getCreatureById: GetCreatureByIdUseCase,
    val getCreatureByIdAndSubCategoryUseCase: GetCreatureByIdAndSubCategoryUseCase,
    val getCreatureByRelationAndSubCategory: GetCreatureByRelationAndSubCategory,
    val getMainBossesUseCase: GetMainBossesUseCase,
    val getMiniBossesUseCase: GetMiniBossesUseCase,
    val getAggressiveCreatures: GetAggressiveCreatures,
    val getPassiveCreature: GetPassiveCreature,
    val getNPCsUseCase: GetNPCsUseCase,

    val getLocalCreaturesUseCase: GetLocalCreaturesUseCase,
    val refetchCreaturesUseCase: RefetchCreaturesUseCase,
)