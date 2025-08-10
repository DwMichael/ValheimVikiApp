package com.rabbitv.valheimviki.domain.use_cases.creature.get_passive_creatures

import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreatures
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetPassiveCreature @Inject constructor(private val creatureRepository: CreatureRepository) {
	operator fun invoke(): Flow<List<PassiveCreature>> =
		creatureRepository.getCreaturesBySubCategory(CreatureSubCategory.PASSIVE_CREATURE.toString())
			.map { creatures -> creatures.toPassiveCreatures() }
}