package com.rabbitv.valheimviki.domain.use_cases.creature.get_main_bosses


import com.rabbitv.valheimviki.data.mappers.creatures.toMainBosses
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetMainBossesUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<MainBoss>> {
        val creatureSubCategory = CreatureSubCategory.BOSS
        return creatureRepository.getCreaturesBySubCategory(creatureSubCategory.toString())
            .map { mainBosses -> mainBosses.toMainBosses().sortedBy { it.order } }
    }
}