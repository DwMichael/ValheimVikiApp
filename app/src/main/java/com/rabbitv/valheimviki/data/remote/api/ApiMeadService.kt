package com.rabbitv.valheimviki.data.remote.api


import com.rabbitv.valheimviki.domain.model.mead.Mead
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiMeadService {
    @GET("/Mead")
    suspend fun fetchMeads(@Query("lang") lang: String): Response<List<Mead>>
}