package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.relation.Relation
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RelationsRepository {
    fun getLocalRelations(): Flow<List<Relation>>
    fun getRelatedId(queryId: String): String
    fun getRelatedIds(queryId: String): List<String>
    suspend fun insertRelations(relations: List<Relation>)
    suspend fun fetchRelations(): Response<List<Relation>>
    suspend fun fetchAndInsertRelations()
}