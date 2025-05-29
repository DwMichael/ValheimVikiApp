package com.rabbitv.valheimviki.domain.use_cases.creature.get_npcs

import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetNPCsUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<NPC>> {
        val creatureSubCategory = CreatureSubCategory.NPC
        return creatureRepository.getCreaturesBySubCategory(creatureSubCategory.toString())
            .map { mainBosses -> mainBosses.toNPC().sortedBy { it.order } }
    }
}