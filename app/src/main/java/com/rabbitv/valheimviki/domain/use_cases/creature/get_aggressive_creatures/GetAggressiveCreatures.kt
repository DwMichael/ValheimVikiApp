package com.rabbitv.valheimviki.domain.use_cases.creature.get_aggressive_creatures

import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAggressiveCreatures @Inject constructor(private val creatureRepository: CreatureRepository) {
	operator fun invoke(): Flow<List<AggressiveCreature>> =
		creatureRepository.getCreaturesBySubCategory(CreatureSubCategory.AGGRESSIVE_CREATURE.toString())
			.map { mainBosses -> mainBosses.toAggressiveCreatures() }

}