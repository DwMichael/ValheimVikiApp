package com.rabbitv.valheimviki.domain.use_cases.creature.get_local_creatures

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocalCreaturesUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
	operator fun invoke(): Flow<List<Creature>> {
		return creatureRepository.getLocalCreatures().map { creatures ->
			creatures.sortedBy { it.order }
		}
	}
}