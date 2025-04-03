package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import kotlinx.coroutines.flow.Flow

interface CreaturesRepository {
    fun getLocalMainBosses(): Flow<List<MainBoss>>
    fun getMainBossById(id:String): Flow<MainBoss>
    suspend fun fetchMainBosses(lang: String): List<MainBoss>
    suspend fun insertLocalCreatures(creatures: List<Creature>)

}