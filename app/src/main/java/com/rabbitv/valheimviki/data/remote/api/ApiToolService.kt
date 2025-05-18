package com.rabbitv.valheimviki.data.remote.api


import com.example.domain.entities.tool.Tool
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiToolService {

    @GET("/Tools")
    suspend fun fetchTools(@Query("lang") lang: String): Response<List<Tool>>
}