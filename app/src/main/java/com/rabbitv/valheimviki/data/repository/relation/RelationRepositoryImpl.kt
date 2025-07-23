package com.rabbitv.valheimviki.data.repository.relation


import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RelationRepositoryImpl @Inject constructor(
	private val apiService: ApiRelationsService,
	private val relationDao: RelationDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : RelationRepository {
	override fun getLocalRelations(): Flow<List<Relation>> {
		return relationDao.getLocalRelations().flowOn(ioDispatcher)
	}

	override fun getRelatedId(queryId: String): Flow<String?> {
		return relationDao.getRelatedId(queryId).flowOn(ioDispatcher)
	}

	override fun getRelatedIdsFor(queryId: String): Flow<List<RelatedItem>> {
		return relationDao.getRelatedIdsFor(queryId).flowOn(ioDispatcher)
	}

	override fun getRelatedIds(queryId: String): Flow<List<RelatedItem>> {
		return relationDao.getRelatedIds(queryId).flowOn(ioDispatcher)
	}

	override suspend fun insertRelations(relations: List<Relation>) {
		check(relations.isNotEmpty()) { "Relation list cannot be empty, cannot insert ${relations.size} relations" }
		withContext(ioDispatcher) {
			relationDao.insertRelations(relations)
		}

	}

	override suspend fun fetchRelations(): Response<List<Relation>> {
		return withContext(ioDispatcher) {
			apiService.fetchRelations()
		}
	}

}