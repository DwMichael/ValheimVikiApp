package com.rabbitv.valheimviki.domain.use_cases.creature.get_npcs

import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetNPCsUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
	operator fun invoke(): Flow<List<NPC>> =
		creatureRepository.getCreaturesBySubCategory(CreatureSubCategory.NPC.toString())
			.map { mainBosses -> mainBosses.toNPC() }
}