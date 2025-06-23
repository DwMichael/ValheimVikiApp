package com.rabbitv.valheimviki.data.repository.mead

import com.rabbitv.valheimviki.data.local.dao.MeadDao
import com.rabbitv.valheimviki.data.remote.api.ApiMeadService
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.repository.MeadRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MeadRepositoryImpl @Inject constructor(
    private val apiService: ApiMeadService,
    private val meadDao: MeadDao,
) : MeadRepository {
    override suspend fun fetchMeads(language: String): Response<List<Mead>> {
        return apiService.fetchMeads(language)
    }

    override fun getLocalMeads(): Flow<List<Mead>> {
        return meadDao.getLocalMeads()
    }

    override fun getMeadsByIds(ids: List<String>): Flow<List<Mead>> {
        return meadDao.getMeadsByIds(ids)
    }

    override fun getMeadById(id: String): Flow<Mead?> {
        return meadDao.getMeadById(id)
    }

    override fun getMeadsBySubCategory(subCategory: String): Flow<List<Mead>> {
        return meadDao.getMeadsBySubCategory(subCategory)
    }

    override suspend fun insertMeads(meads: List<Mead>) {
        check(meads.isNotEmpty()) { "Meads list cannot be empty , cannot insert ${meads.size} meads" }
        meadDao.insertMeads(meads)
    }

}