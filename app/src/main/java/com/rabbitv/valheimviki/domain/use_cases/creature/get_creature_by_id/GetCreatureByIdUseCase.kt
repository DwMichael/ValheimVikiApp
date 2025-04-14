package com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id

import com.rabbitv.valheimviki.domain.exceptions.CreaturesByIdFetchLocalException
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class GetCreatureByIdUseCase@Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(creatureId: String): Creature {
       try {
           return creatureRepository.getCreatureById(creatureId) ?:throw CreaturesByIdFetchLocalException("Creature with id $creatureId not found")
       }catch (e : Exception)
       {
           throw CreaturesByIdFetchLocalException("Error fetching from Room creature by id : ${e.message}")
       }
    }
}