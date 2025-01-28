package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService

import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApiCreatureRepository @Inject constructor(private val creatureService: ApiCreatureService) {

    fun getAllCreatures(lang:String): Flow<List<CreatureDtoX>> {
        return flow {
            emit(creatureService.getAllCreatures(lang))
        }.map {
            it.creatures
        }
    }
}