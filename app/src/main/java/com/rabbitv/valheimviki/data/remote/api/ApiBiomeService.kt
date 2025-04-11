package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.biome.Biome
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiBiomeService {
    @GET("Biomes")
    suspend fun fetchBiomes(
        @Query("lang") lang: String = "en"
    ): Response<List<Biome>>
}