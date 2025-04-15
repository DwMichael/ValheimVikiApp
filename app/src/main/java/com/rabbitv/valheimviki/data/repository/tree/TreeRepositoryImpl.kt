package com.rabbitv.valheimviki.data.repository.tree

import com.rabbitv.valheimviki.data.local.dao.TreeDao
import com.rabbitv.valheimviki.data.remote.api.ApiTreeService
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class TreeRepositoryImpl @Inject constructor(
    private val apiService: ApiTreeService,
    private val treeDao: TreeDao
): TreeRepository
{
    override fun getLocalTrees(): Flow<List<Tree>> {
        return treeDao.getLocalTrees()
    }

    override fun getTreeById(id: String): Tree? {
        return treeDao.getTreeById(id)
    }

    override fun getTreesByIds(ids: List<String>): List<Tree> {
        return treeDao.getTreesByIds(ids)
    }

    override suspend fun fetchTrees(lang:String): Response<List<Tree>> {
        return apiService.fetchTrees(lang)
    }


    override suspend fun insertTrees(trees: List<Tree>) {
        treeDao.insertTrees(trees)
    }

}