package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.remote.api.CreatureRepository
import com.rabbitv.valheimviki.domain.model.CreatureDtoX
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatureRepositoryImpl @Inject constructor(
    private val apiService: ApiCreatureService,
    private val creatureDao: CreatureDao
):CreatureRepository {
    override fun getAllCreatures(lang: String): Flow<List<CreatureDtoX>> {
       return creatureDao.getAllCreatures()
    }

    override suspend fun refreshCreatures(lang: String) {
        val creatures = apiService.getAllCreatures(lang)
        creatureDao.insertAllCreatures(creatures.creatures)
    }


}