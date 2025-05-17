package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeaponService {
    @GET("Weapons")
    suspend fun fetchWeapons(
        @Query("lang") lang: String
    ): Response<List<Weapon>>

}