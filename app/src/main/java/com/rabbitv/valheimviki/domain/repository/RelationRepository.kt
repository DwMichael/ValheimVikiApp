package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.relation.Relation
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RelationRepository {
    fun getLocalRelations(): Flow<List<Relation>>
    fun getRelatedId(queryId: String): Flow<String?>
    fun getRelatedIds(queryId: String): Flow<List<RelatedItem>>
    suspend fun insertRelations(relations: List<Relation>)
    suspend fun fetchRelations(): Response<List<Relation>>
}