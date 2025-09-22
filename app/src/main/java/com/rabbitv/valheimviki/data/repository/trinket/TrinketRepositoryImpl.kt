package com.rabbitv.valheimviki.data.repository.trinket

import com.rabbitv.valheimviki.data.local.dao.TrinketDao
import com.rabbitv.valheimviki.data.remote.api.ApiTrinketService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.repository.TrinketRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class TrinketRepositoryImpl @Inject constructor(
	private val apiService: ApiTrinketService,
	private val trinketDao: TrinketDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TrinketRepository {
	override fun getLocalTrinkets(): Flow<List<Trinket>> {
		return trinketDao.getLocalTrinkets().flowOn(ioDispatcher)
	}

	override fun getTrinketById(id: String): Flow<Trinket?> {
		return trinketDao.getTrinketById(id).flowOn(ioDispatcher)
	}

	override fun getTrinketsByIds(ids: List<String>): Flow<List<Trinket>> {
		return trinketDao.getTrinketsByIds(ids).flowOn(ioDispatcher)
	}

	override suspend fun fetchTrinkets(lang: String): Response<List<Trinket>> {
		return withContext(ioDispatcher) { apiService.fetchTrinkets(lang) }
	}

	override suspend fun insertTrinkets(trees: List<Trinket>) {
		withContext(ioDispatcher) {
			trinketDao.insertTrinkets(trees)
		}
	}
}