package com.rabbitv.valheimviki.data.repository.creatures

import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchAndInsertException
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
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
        check(creatures.isNotEmpty()){"Creature list cannot be empty , cannot insert ${creatures.size} creatures"}
        creatureDao.insertCreatures(creatures)
    }

    override suspend fun fetchCreature(lang: String): Response<List<Creature>> {
            return apiService.fetchCreatures(lang)
    }

    override suspend fun fetchCreatureAndInsert(lang: String) = coroutineScope {

        val deferred : Deferred<List<Creature>> = async(Dispatchers.IO) {
            getAllCreatures()
        }
        val creatureList = deferred.await()
        if (creatureList.size != 81) {
            try {
                val response = fetchCreature(lang)
                val relationsList = response.body()

                if (response.isSuccessful && relationsList?.isNotEmpty() == true) {
                    try {
                        insertCreatures(relationsList)
                    } catch (e: Exception) {
                        throw CreatureFetchException("Error inserting creatures: ${e.message}")
                    }
                } else {
                    throw CreatureFetchException("Fetching creatures failed : ${response.errorBody()}")
                }
            } catch (e: Exception) {
                throw CreatureFetchAndInsertException("Error fetching and inserting creatures: ${e.message}")
            }
        }
    }
}
