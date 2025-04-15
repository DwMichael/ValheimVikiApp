package com.rabbitv.valheimviki.data.repository.relation


import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RelationRepositoryImpl(
    private val apiService: ApiRelationsService,
    private val relationDao: RelationDao
) : RelationRepository {
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
        check(relations.isNotEmpty()) { "Relation list cannot be empty, cannot insert ${relations.size} relations" }
        relationDao.insertRelations(relations)
    }

    override suspend fun fetchRelations(): Response<List<Relation>> {
            return apiService.fetchRelations()
    }

}