package com.rabbitv.valheimviki.domain.use_cases.creature.get_aggressive_creatures

import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesInsertException
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAggressiveCreatures@Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<AggressiveCreature>> {

        val creatureType = CreatureType.AGGRESSIVE_CREATURE
        return creatureRepository.getCreaturesBySubCategory(creatureType.toString()).map { mainBosses -> mainBosses.toAggressiveCreatures().sortedBy { it.order } }

    }
}