package com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_boss_by_id

import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetMainBossByIdUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(mainBossId:String): Flow<MainBoss>  {
        return creatureRepository.getMainBossById(mainBossId)
    }
}