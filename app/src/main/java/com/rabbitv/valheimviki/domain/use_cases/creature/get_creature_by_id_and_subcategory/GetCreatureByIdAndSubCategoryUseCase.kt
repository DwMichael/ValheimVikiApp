package com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id_and_subcategory

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCreatureByIdAndSubCategoryUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(id: String, creatureSubCategory: CreatureSubCategory): Flow<Creature?> {
        return creatureRepository.getCreatureByIdAndSubCategory(
            id,
            creatureSubCategory.toString()
        ).flowOn(Dispatchers.IO)
    }
}