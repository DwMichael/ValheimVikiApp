package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.exceptions.NetworkExceptionHandler
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

            val response = apiService.getAllCreatures(lang)

            return if (response.success) {
                CreatureDto(
                    creatures = response.creatures,
                    error = null,
                    success = true,
                    errorDetails = null
                )
            } else {
                CreatureDto(
                    creatures = emptyList(),
                    error = response.error,
                    success = false,
                    errorDetails = response.errorDetails
                )
            }

        } catch (e: Exception) {
            val networkException = NetworkExceptionHandler.handleException(e)
            return CreatureDto(
                creatures = emptyList(),
                error = networkException.error,
                success = networkException.success,
                errorDetails = networkException.errorDetails
            )
        }
    }

    override suspend fun storeCreatures(creatures: List<CreatureDtoX>) {
        if (creatures.isNotEmpty()) {
            creatureDao.insertAllCreatures(creatures)
        }
    }


}