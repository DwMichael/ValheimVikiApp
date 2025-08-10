package com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_subcategory

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCreatureBySubCategoryUseCase @Inject constructor(
	private val creatureRepository: CreatureRepository
) {
	operator fun invoke(subCategory: CreatureSubCategory): Flow<List<Creature>> =
		creatureRepository.getCreaturesBySubCategory(subCategory.toString())
}