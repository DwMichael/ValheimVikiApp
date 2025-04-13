package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiOreDepositService {
    @GET("Ore_deposits")
    suspend fun fetchOreDeposits(
        @Query("lang") lang: String = "en"
    ): Response<List<OreDeposit>>
}