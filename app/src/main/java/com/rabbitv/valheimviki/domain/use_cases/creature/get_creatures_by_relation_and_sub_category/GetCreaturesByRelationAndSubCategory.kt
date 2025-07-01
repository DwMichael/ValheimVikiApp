package com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_relation_and_sub_category

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetCreaturesByRelationAndSubCategory @Inject constructor(
	private val creatureRepository: CreatureRepository
) {
	operator fun invoke(
		creatureIds: List<String>,
		creatureSubCategory: CreatureSubCategory
	): Flow<List<Creature>> {
		return creatureRepository.getCreaturesByRelationAndSubCategory(
			creatureIds,
			creatureSubCategory.toString()
		).flowOn(Dispatchers.IO)
	}
}