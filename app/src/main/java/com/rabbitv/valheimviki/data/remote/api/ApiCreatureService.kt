package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.creature.Creature
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCreatureService {

    @GET("Creatures")
    suspend fun fetchCreatures(
        @Query("lang") lang: String = "en"
    ): Response<List<Creature>>

//    @GET("Bosses")
//    suspend fun fetchMainBosses(
//        @Query("lang") lang: String = "en"
//    ): Response<List<Creature>>
//
//    @GET("MiniBosses")
//    suspend fun fetchMiniBosses(
//        @Query("lang") lang: String = "en"
//    ): Response<List<Creature>>

}