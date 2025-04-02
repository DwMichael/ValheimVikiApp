package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.relation.Relation
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RelationsRepository {
    fun getLocalRelations(): Flow<List<Relation>>
    fun getItemIdInRelation(queryId: String): Flow<String>
    suspend fun insertRelations(relations: List<Relation>)
    suspend fun fetchRelations(): Response<List<Relation>>
}