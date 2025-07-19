package com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCreatureByIdUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
	operator fun invoke(creatureId: String): Flow<Creature?> {
		return creatureRepository.getCreatureById(creatureId)

	}
}