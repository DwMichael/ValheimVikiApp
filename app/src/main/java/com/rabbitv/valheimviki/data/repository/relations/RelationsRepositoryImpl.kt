package com.rabbitv.valheimviki.data.repository.relations


import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RelationsRepositoryImpl (
    private val apiService: ApiRelationsService,
    private val relationDao: RelationDao
) : RelationsRepository{
    override fun getLocalRelations(): Flow<List<Relation>> {
        return relationDao.getLocalRelations()
    }

    override fun getItemIdInRelation(queryId: String): Flow<String> {
        return relationDao.getItemIdInRelation(queryId)
    }

    override suspend fun insertRelations(relations: List<Relation>) {
        return relationDao.insertRelations(relations)
    }

    override suspend fun fetchRelations(): Response<List<Relation>> {
        try {
            return apiService.fetchRelations()
        }catch (exception: Exception)
        {
            throw exception
        }

    }
}