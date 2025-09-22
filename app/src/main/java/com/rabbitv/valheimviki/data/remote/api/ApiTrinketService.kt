package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiTrinketService {
	@GET("Trinkets")
	suspend fun fetchTrinkets(
		@Query("lang") lang: String
	): Response<List<Trinket>>

}