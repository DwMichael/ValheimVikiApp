package com.rabbitv.valheimviki.domain.use_cases.creature.get_aggressive_creatures

import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAggressiveCreatures @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<AggressiveCreature>> {

        val creatureSubCategory = CreatureSubCategory.AGGRESSIVE_CREATURE
        return creatureRepository.getCreaturesBySubCategory(creatureSubCategory.toString())
            .map { mainBosses -> mainBosses.toAggressiveCreatures().sortedBy { it.order } }

    }
}