package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import kotlinx.coroutines.flow.Flow

interface BiomeRepository{
    fun getAllBiomes(lang:String): Flow<List<BiomeDtoX>>
    suspend fun refreshBiomes(lang: String)

}
