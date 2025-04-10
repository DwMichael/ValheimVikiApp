package com.rabbitv.valheimviki.data.repository.creatures

import android.util.Log
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CreaturesRepositoryImpl @Inject constructor(
    private val apiService: ApiCreatureService,
    private val creatureDao: CreatureDao
) : CreaturesRepository {



    override fun getAllCreatures(): List<Creature> {
        return creatureDao.getAllCreatures()
    }

    override fun getCreaturesBySubCategory(subCategory: String): Flow<List<Creature>> {
        return creatureDao.getCreaturesBySubCategory(subCategory)
    }

    override fun getCreatureByIdAndSubCategory(id: String, subCategory: String): Creature {
        return creatureDao.getCreatureByIdAndSubCategory(id, subCategory)
    }

    override fun getCreaturesByIds(ids: List<String>): List<Creature> {
        return creatureDao.getCreaturesByIds(ids)
    }

    override fun getCreatureById(id: String): Creature {
        return creatureDao.getCreatureById(id)
    }

    override suspend fun insertCreatures(creatures: List<Creature>) {
        if (creatures.isNotEmpty()) {
            creatureDao.insertCreatures(creatures)
        }
    }

    override suspend fun fetchCreature(lang: String): Response<List<Creature>> {
        try {
            return apiService.fetchCreatures(lang)
        } catch (exception: Exception) {
            Log.i("EXEPTION FETCH", exception.message.toString())
            throw exception
        }
    }

    override suspend fun fetchCreatureAndInsert(lang: String) = coroutineScope {
        val deferred : Deferred<List<Creature>> = async(Dispatchers.IO) {
                getAllCreatures()
        }
            val creatureList = deferred.await()
        if(creatureList.size != 71) {
            val response = fetchCreature(lang)
            val creaturesList = response.body()

            if (response.isSuccessful && creaturesList != null) {
                insertCreatures(creaturesList)
            }
        }
    }

}
