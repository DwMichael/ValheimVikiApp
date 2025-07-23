package com.rabbitv.valheimviki.data.repository.mead

import com.rabbitv.valheimviki.data.local.dao.MeadDao
import com.rabbitv.valheimviki.data.remote.api.ApiMeadService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.repository.MeadRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class MeadRepositoryImpl @Inject constructor(
	private val apiService: ApiMeadService,
	private val meadDao: MeadDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MeadRepository {
	override suspend fun fetchMeads(language: String): Response<List<Mead>> {
		return withContext(ioDispatcher) {
			apiService.fetchMeads(language)
		}
	}

	override fun getLocalMeads(): Flow<List<Mead>> {
		return meadDao.getLocalMeads().flowOn(ioDispatcher)
	}

	override fun getMeadsByIds(ids: List<String>): Flow<List<Mead>> {
		return meadDao.getMeadsByIds(ids).flowOn(ioDispatcher)
	}

	override fun getMeadById(id: String): Flow<Mead?> {
		return meadDao.getMeadById(id).flowOn(ioDispatcher)
	}

	override fun getMeadsBySubCategory(subCategory: String): Flow<List<Mead>> {
		return meadDao.getMeadsBySubCategory(subCategory).flowOn(ioDispatcher)
	}

	override suspend fun insertMeads(meads: List<Mead>) {
		check(meads.isNotEmpty()) { "Meads list cannot be empty , cannot insert ${meads.size} meads" }
		withContext(ioDispatcher)
		{
			meadDao.insertMeads(meads)
		}

	}

}