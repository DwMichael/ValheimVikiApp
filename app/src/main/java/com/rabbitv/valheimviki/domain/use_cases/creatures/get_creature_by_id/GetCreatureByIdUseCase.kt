package com.rabbitv.valheimviki.domain.use_cases.creatures.get_creature_by_id

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCreatureByIdUseCase@Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(creatureId: String): Flow<Creature> = creatureRepository.getCreatureById(creatureId)
}