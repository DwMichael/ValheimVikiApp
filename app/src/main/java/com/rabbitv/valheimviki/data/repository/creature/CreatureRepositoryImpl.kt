package com.rabbitv.valheimviki.data.repository.creature

import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class CreatureRepositoryImpl @Inject constructor(
	private val apiService: ApiCreatureService,
	private val creatureDao: CreatureDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CreatureRepository {


	override fun getLocalCreatures(): Flow<List<Creature>> {
		return creatureDao.getLocalCreatures().flowOn(ioDispatcher)
	}

	override fun getCreaturesBySubCategory(subCategory: String): Flow<List<Creature>> {
		return creatureDao.getCreaturesBySubCategory(subCategory).flowOn(ioDispatcher)
	}

	override fun getCreatureByIdAndSubCategory(id: String, subCategory: String): Flow<Creature?> {
		return creatureDao.getCreatureByIdAndSubCategory(id, subCategory).flowOn(ioDispatcher)
	}

	override fun getCreatureByRelationAndSubCategory(
		creaturesIds: List<String>,
		subCategory: String
	): Flow<Creature?> {
		return creatureDao.getCreatureByRelationAndSubCategory(creaturesIds, subCategory)
			.flowOn(ioDispatcher)
	}

	override fun getCreaturesByIds(ids: List<String>): Flow<List<Creature>> {
		return creatureDao.getCreaturesByIds(ids).flowOn(ioDispatcher)
	}

	override fun getCreatureById(id: String): Flow<Creature?> {
		return creatureDao.getCreatureById(id).flowOn(ioDispatcher)
	}

	override fun getCreaturesByRelationAndSubCategory(
		ids: List<String>,
		subCategory: String
	): Flow<List<Creature>> {
		return creatureDao.getCreaturesByRelationAndSubCategory(ids, subCategory)
			.flowOn(ioDispatcher)
	}

	override suspend fun insertCreatures(creatures: List<Creature>) {
		check(creatures.isNotEmpty()) { "Creature list cannot be empty , cannot insert ${creatures.size} creatures" }
		withContext(ioDispatcher) {
			creatureDao.insertCreatures(creatures)
		}

	}

	override suspend fun fetchCreatures(lang: String): Response<List<Creature>> {
		return withContext(ioDispatcher)
		{
			apiService.fetchCreatures(lang)
		}
	}


}
