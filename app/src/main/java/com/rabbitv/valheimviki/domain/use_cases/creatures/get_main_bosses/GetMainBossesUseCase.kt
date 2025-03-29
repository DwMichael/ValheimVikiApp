package com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_bosses


import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetMainBossesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<MainBoss>> {
       return creatureRepository.getLocalMainBosses()
           .flatMapConcat {localMainBoss ->
               if(localMainBoss.isNotEmpty())
               {
                   flowOf(localMainBoss)
               }else
               {
                   try {
                       val mainBossList = creatureRepository.fetchMainBosses(language)
                       creatureRepository.storeMainBosses(mainBossList)
                       creatureRepository.getLocalMainBosses()
                   } catch (e: Exception)
                   {
                        throw FetchException("No local data available and failed to fetch from internet.")
                    }
               }
           }.map { mainBosses -> mainBosses.sortedBy { it.order } }
    }
}