package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCraftingService {
    @GET("/Crafting")
    suspend fun fetchCraftingStations(@Query("lang") lang: String): Response<List<CraftingObject>>
}