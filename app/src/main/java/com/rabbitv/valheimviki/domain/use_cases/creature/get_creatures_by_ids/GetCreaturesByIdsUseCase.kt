package com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_ids

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCreaturesByIdsUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
	operator fun invoke(ids: List<String>): Flow<List<Creature>> =
		creatureRepository.getCreaturesByIds(ids)


}