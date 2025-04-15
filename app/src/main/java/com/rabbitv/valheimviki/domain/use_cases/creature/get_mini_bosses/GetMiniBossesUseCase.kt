package com.rabbitv.valheimviki.domain.use_cases.creature.get_mini_bosses

import com.rabbitv.valheimviki.data.mappers.creatures.toMiniBosses
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMiniBossesUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<MiniBoss>> {
        val creatureType = CreatureType.MINI_BOSS
        return creatureRepository.getCreaturesBySubCategory(creatureType.toString())
          .map { mainBosses -> mainBosses.toMiniBosses().sortedBy { it.order } }
    }
}