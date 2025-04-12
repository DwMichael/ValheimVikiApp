package com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_bosses


import com.rabbitv.valheimviki.data.mappers.toMainBosses
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetMainBossesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<MainBoss>> {
        val creatureType = CreatureType.BOSS
        return creatureRepository.getCreaturesBySubCategory(creatureType.toString())
            .map { mainBosses -> mainBosses.toMainBosses().sortedBy { it.order } }
    }
}