package com.rabbitv.valheimviki.data.repository.creatures

import android.util.Log
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.utils.bodyList
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CreaturesRepositoryImpl @Inject constructor(
    private val apiService: ApiCreatureService,
    private val creatureDao: CreatureDao
) : CreaturesRepository {


    private fun checkResponse(response: Response<List<Creature>>): List<Creature> {
        return if (response.isSuccessful) {
            response.bodyList()
        } else {
            emptyList()
        }
    }

    override fun getCreaturesBySubCategory(subCategory: String): Flow<List<Creature>> {
        return creatureDao.getCreaturesBySubCategory(subCategory)
    }

    override fun getCreatureByIdAndSubCategory(id: String, subCategory: String): Flow<Creature> {
        return creatureDao.getCreatureByIdAndSubCategory(id, subCategory)
    }

    override fun getCreaturesByIds(ids: List<String>): Flow<List<Creature>> {
        return creatureDao.getCreaturesByIds(ids)
    }

    override fun getCreatureById(id: String): Flow<Creature> {
        return creatureDao.getCreatureById(id)
    }

    override suspend fun fetchCreatureByType(
        lang: String,
        creatureType: CreatureType
    ): List<Creature> {
        try {
            return when(creatureType) {
                CreatureType.BOSS -> checkResponse(apiService.fetchMainBosses(lang))
                CreatureType.MINI_BOSS -> checkResponse(apiService.fetchMiniBosses(lang))
                CreatureType.AGGRESSIVE_CREATURE -> checkResponse(apiService.fetchAggressiveCreatures(lang))
                CreatureType.PASSIVE_CREATURE -> checkResponse(apiService.fetchPassiveCreature(lang))
                CreatureType.NPC -> checkResponse(apiService.fetchNPCs(lang))
            }
        } catch (exception: Exception) {
            Log.i("EXEPTION FETCH", exception.message.toString())
            return emptyList()
        }
    }


    override suspend fun insertLocalCreatures(creatures: List<Creature>) {
        if (creatures.isNotEmpty()) {
            creatureDao.insertCreatures(creatures)
        }
    }
}
