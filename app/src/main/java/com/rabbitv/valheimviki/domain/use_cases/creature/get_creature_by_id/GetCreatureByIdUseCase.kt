package com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCreatureByIdUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(creatureId: String): Flow<Creature?> {
        return creatureRepository.getCreatureById(creatureId).flowOn(Dispatchers.IO)

    }
}