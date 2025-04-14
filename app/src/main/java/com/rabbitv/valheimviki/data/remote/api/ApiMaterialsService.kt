package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.material.Material
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMaterialsService {
    @GET("/Materials")
    suspend fun fetchMaterials( @Query("lang") lang: String = "en"): Response<List<Material>>

}