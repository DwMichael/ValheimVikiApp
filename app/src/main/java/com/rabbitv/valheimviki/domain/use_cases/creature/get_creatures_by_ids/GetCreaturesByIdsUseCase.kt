package com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_ids

import com.rabbitv.valheimviki.domain.exceptions.CreaturesByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class GetCreaturesByIdsUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(ids: List<String>): List<Creature> {
        return try {
            val creatures = creatureRepository.getCreaturesByIds(ids)
            if (creatures.isNotEmpty()) {
                 creatures.sortedBy { it.order }
            }else
            {
                throw CreaturesByIdsFetchLocalException("No creatures found with ids $ids")
            }
        } catch (e: Exception) {
            throw CreaturesByIdsFetchLocalException("Error fetching from Room creatures by ids : ${e.message}")
        }
    }

}