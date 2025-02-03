package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import kotlinx.coroutines.flow.Flow

interface CreatureRepository {
    fun getAllCreatures(): Flow<List<CreatureDtoX>>
    suspend fun refreshCreatures(lang: String)

}