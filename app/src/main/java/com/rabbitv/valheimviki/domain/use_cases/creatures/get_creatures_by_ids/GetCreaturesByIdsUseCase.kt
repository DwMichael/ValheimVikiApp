package com.rabbitv.valheimviki.domain.use_cases.creatures.get_creatures_by_ids

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCreaturesByIdsUseCase@Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(ids: List<String>) : Flow<List<Creature>> = creatureRepository.getCreaturesByIds(ids)
}