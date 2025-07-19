package com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_relation_and_sub_category

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCreatureByRelationAndSubCategory @Inject constructor(
	private val creatureRepository: CreatureRepository
) {
	operator fun invoke(
		creatureIds: List<String>,
		creatureSubCategory: CreatureSubCategory
	): Flow<Creature?> {
		return creatureRepository.getCreatureByRelationAndSubCategory(
			creatureIds,
			creatureSubCategory.toString()
		)
	}
}