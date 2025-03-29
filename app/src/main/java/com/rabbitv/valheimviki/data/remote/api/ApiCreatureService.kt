package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCreatureService {
    @GET("Bosses")
    suspend fun fetchMainBosses(
        @Query("lang") lang: String = "en"
    ): Response<List<MainBoss>>

}