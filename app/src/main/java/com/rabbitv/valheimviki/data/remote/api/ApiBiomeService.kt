package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.biome.BiomeDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiBiomeService {
    @GET("Biomes")
    suspend fun getAllBiomes(
        @Query("lang") lang: String = "en"
    ): BiomeDto
}