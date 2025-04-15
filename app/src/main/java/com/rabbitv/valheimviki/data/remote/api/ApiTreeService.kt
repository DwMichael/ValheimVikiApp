package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.tree.Tree
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiTreeService {
    @GET("Trees")
    suspend fun fetchTrees(
        @Query("lang") lang: String
    ): Response<List<Tree>>

}