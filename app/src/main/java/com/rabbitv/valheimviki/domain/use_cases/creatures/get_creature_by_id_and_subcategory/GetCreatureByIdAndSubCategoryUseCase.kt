package com.rabbitv.valheimviki.domain.use_cases.creatures.get_creature_by_id_and_subcategory

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class GetCreatureByIdAndSubCategoryUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(id: String, creatureType: CreatureType): Creature {
        return creatureRepository.getCreatureByIdAndSubCategory(
            id,
            creatureType.toString()
        )
    }
}