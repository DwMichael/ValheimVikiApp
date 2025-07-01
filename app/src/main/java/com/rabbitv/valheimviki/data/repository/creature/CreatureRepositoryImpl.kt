package com.rabbitv.valheimviki.data.repository.creature

import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CreatureRepositoryImpl @Inject constructor(
	private val apiService: ApiCreatureService,
	private val creatureDao: CreatureDao
) : CreatureRepository {


	override fun getLocalCreatures(): Flow<List<Creature>> {
		return creatureDao.getLocalCreatures()
	}

	override fun getCreaturesBySubCategory(subCategory: String): Flow<List<Creature>> {
		return creatureDao.getCreaturesBySubCategory(subCategory)
	}

	override fun getCreatureByIdAndSubCategory(id: String, subCategory: String): Flow<Creature?> {
		return creatureDao.getCreatureByIdAndSubCategory(id, subCategory)
	}

	override fun getCreatureByRelationAndSubCategory(
		creaturesIds: List<String>,
		subCategory: String
	): Flow<Creature?> {
		return creatureDao.getCreatureByRelationAndSubCategory(creaturesIds, subCategory)
	}

	override fun getCreaturesByIds(ids: List<String>): Flow<List<Creature>> {
		return creatureDao.getCreaturesByIds(ids)
	}

	override fun getCreatureById(id: String): Flow<Creature?> {
		return creatureDao.getCreatureById(id)
	}

	override fun getCreaturesByRelationAndSubCategory(
		ids: List<String>,
		subCategory: String
	): Flow<List<Creature>> {
		return creatureDao.getCreaturesByRelationAndSubCategory(ids, subCategory)
	}

	override suspend fun insertCreatures(creatures: List<Creature>) {
		check(creatures.isNotEmpty()) { "Creature list cannot be empty , cannot insert ${creatures.size} creatures" }
		creatureDao.insertCreatures(creatures)
	}

	override suspend fun fetchCreatures(lang: String): Response<List<Creature>> {
		return apiService.fetchCreatures(lang)
	}


}
