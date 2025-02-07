package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.creature.CreatureDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCreatureService {
    @GET("Creatures")
    suspend fun getAllCreatures(
        @Query("lang") lang: String = "en"
    ): CreatureDto

}