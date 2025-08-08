package com.rabbitv.valheimviki.domain.use_cases.creature.get_local_creatures

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalCreaturesUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
	operator fun invoke(): Flow<List<Creature>> = creatureRepository.getLocalCreatures()
}