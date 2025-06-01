package com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_ids

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCreaturesByIdsUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(ids: List<String>): Flow<List<Creature>> {
        return creatureRepository.getCreaturesByIds(ids).flowOn(Dispatchers.IO)
    }

}