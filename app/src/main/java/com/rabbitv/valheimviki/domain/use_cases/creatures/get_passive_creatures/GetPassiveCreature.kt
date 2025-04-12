package com.rabbitv.valheimviki.domain.use_cases.creatures.get_passive_creatures

import com.rabbitv.valheimviki.data.mappers.toPassiveCreatures
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetPassiveCreature @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<PassiveCreature>> {
        val creatureType = CreatureType.PASSIVE_CREATURE
        return creatureRepository.getCreaturesBySubCategory(creatureType.toString())
            .map { mainBosses -> mainBosses.toPassiveCreatures().sortedBy { it.order } }
    }
}