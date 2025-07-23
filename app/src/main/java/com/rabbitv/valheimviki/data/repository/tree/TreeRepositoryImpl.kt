package com.rabbitv.valheimviki.data.repository.tree

import com.rabbitv.valheimviki.data.local.dao.TreeDao
import com.rabbitv.valheimviki.data.remote.api.ApiTreeService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class TreeRepositoryImpl @Inject constructor(
	private val apiService: ApiTreeService,
	private val treeDao: TreeDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TreeRepository {
	override fun getLocalTrees(): Flow<List<Tree>> {
		return treeDao.getLocalTrees().flowOn(ioDispatcher)
	}

	override fun getTreeById(id: String): Flow<Tree?> {
		return treeDao.getTreeById(id).flowOn(ioDispatcher)
	}

	override fun getTreesByIds(ids: List<String>): Flow<List<Tree>> {
		return treeDao.getTreesByIds(ids).flowOn(ioDispatcher)
	}

	override suspend fun fetchTrees(lang: String): Response<List<Tree>> {
		return withContext(ioDispatcher) { apiService.fetchTrees(lang) }
	}
    
	override suspend fun insertTrees(trees: List<Tree>) {
		withContext(ioDispatcher) {
			treeDao.insertTrees(trees)
		}
	}

}