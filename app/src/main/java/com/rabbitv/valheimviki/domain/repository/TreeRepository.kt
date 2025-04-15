package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.tree.Tree
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TreeRepository {
    fun getLocalTrees(): Flow<List<Tree>>
    fun getTreeById(id: String): Tree?
    fun getTreesByIds(ids: List<String>): List<Tree>

    suspend fun fetchTrees(lang:String): Response<List<Tree>>
    suspend fun insertTrees(trees: List<Tree>)
}