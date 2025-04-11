package com.rabbitv.valheimviki.data.repository.relations


import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.domain.exceptions.RelationFetchAndInsertException
import com.rabbitv.valheimviki.domain.exceptions.RelationFetchException
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.Response

class RelationsRepositoryImpl(
    private val apiService: ApiRelationsService,
    private val relationDao: RelationDao
) : RelationsRepository {
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
        try {
            return apiService.fetchRelations()
        } catch (e: Exception) {
            throw RelationFetchException("Error fetching relations: ${e.message}")
        }
    }


    override suspend fun fetchAndInsertRelations() {
        val localRelations = getLocalRelations().first()

        if (localRelations.isEmpty()) {
            try {
                val response = fetchRelations()
                val relationsList = response.body()

                if (response.isSuccessful && relationsList?.isNotEmpty() == true) {
                    try {
                        insertRelations(relationsList)
                    } catch (e: Exception) {
                        throw RelationFetchException("Error inserting relations: ${e.message}")
                    }
                } else {
                    throw RelationFetchException("FetchAndInsertRelations failed : ${response.errorBody()}")
                }
            } catch (e: Exception) {
                throw RelationFetchAndInsertException("Error fetching and inserting relations: ${e.message}")
            }
        }
    }
}