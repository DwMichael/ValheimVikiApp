package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import kotlinx.coroutines.flow.Flow

interface CreatureRepository {
    fun getAllCreatures(lang:String): Flow<List<CreatureDtoX>>
    suspend fun refreshCreatures(lang: String)

}