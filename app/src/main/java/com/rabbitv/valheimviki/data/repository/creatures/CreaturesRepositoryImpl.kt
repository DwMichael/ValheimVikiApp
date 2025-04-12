package com.rabbitv.valheimviki.data.repository.creatures

import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CreaturesRepositoryImpl @Inject constructor(
    private val apiService: ApiCreatureService,
    private val creatureDao: CreatureDao
) : CreaturesRepository {



    override fun getAllCreatures(): Flow<List<Creature>> {
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

    override fun getCreatureById(id: String): Creature? {
        return creatureDao.getCreatureById(id)
    }

    override suspend fun insertCreatures(creatures: List<Creature>) {
        check(creatures.isNotEmpty()){"Creature list cannot be empty , cannot insert ${creatures.size} creatures"}
        creatureDao.insertCreatures(creatures)
    }

    override suspend fun fetchCreatures(lang: String): Response<List<Creature>> {
            return apiService.fetchCreatures(lang)
    }


}
