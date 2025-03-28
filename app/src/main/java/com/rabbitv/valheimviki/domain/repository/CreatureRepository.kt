package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.creature.CreatureDto
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import kotlinx.coroutines.flow.Flow

interface CreatureRepository {
    fun getAllCreatures(): Flow<List<CreatureDtoX>>
    fun getMainBosses(): Flow<List<CreatureDtoX>>
    fun getMiniBosses(): Flow<List<CreatureDtoX>>
    suspend fun fetchCreatures(lang: String): CreatureDto
    suspend fun storeCreatures(creatures: List<CreatureDtoX>)

}