package com.rabbitv.valheimviki.data.repository.relations


import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.Response

class RelationsRepositoryImpl (
    private val apiService: ApiRelationsService,
    private val relationDao: RelationDao
) : RelationsRepository{
    override fun getLocalRelations(): Flow<List<Relation>> {
        return relationDao.getLocalRelations()
    }

    override fun getRelatedId(queryId: String): String {
        return relationDao.getRelatedId(queryId)
    }

    override fun getRelatedIds(queryId: String): List<String> {
        return relationDao.getRelatedIds(queryId)
    }

    override suspend fun insertRelations(relations: List<Relation>) {
            relationDao.insertRelations(relations)
    }

    override suspend fun fetchRelations(): Response<List<Relation>> {
        try {
            return apiService.fetchRelations()
        }catch (exception: Exception)
        {
            throw exception
        }
    }

    override suspend fun fetchAndInsertRelations() {
        val localRelations = getLocalRelations().first()

        if (localRelations.isEmpty()) {
            val response = fetchRelations()
            val relationsList = response.body()

            if (response.isSuccessful && relationsList != null) {
                insertRelations(relationsList)
            }
        }
    }
}