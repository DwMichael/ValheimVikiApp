package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.remote.exceptions.NetworkExceptionHandler
import com.rabbitv.valheimviki.domain.model.creature.CreatureDto
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatureRepositoryImpl @Inject constructor(
    private val apiService: ApiCreatureService,
    private val creatureDao: CreatureDao
) : CreatureRepository {
    override fun getAllCreatures(): Flow<List<CreatureDtoX>> {
        return creatureDao.getAllCreatures()
    }

    override fun getMainBosses(): Flow<List<CreatureDtoX>> {
        return creatureDao.getMainBosses()
    }

    override fun getMiniBosses(): Flow<List<CreatureDtoX>> {
        return creatureDao.getMiniBosses()
    }

    override suspend fun fetchCreatures(lang: String): CreatureDto {
        try {
            val creatures = apiService.getAllCreatures(lang)
            creatureDao.insertAllCreatures(creatures.creatures)
            return creatures
        } catch (e: Exception) {
            val networkException = NetworkExceptionHandler.handleException(e)
            val creatureDto = CreatureDto(
                creatures = emptyList(),
                error = networkException.error,
                success = networkException.success,
                errorDetails = networkException.errorDetails
            )
            return creatureDto
        }

    }


}