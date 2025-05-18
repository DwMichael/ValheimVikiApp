package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.armor.Armor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiArmorService {
    @GET("Armors")
    suspend fun fetchArmor(
        @Query("lang") lang: String
    ): Response<List<Armor>>

}