package com.rabbitv.valheimviki.data.remote.api


import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiToolService {

	@GET("/Tools")
	suspend fun fetchTools(@Query("lang") lang: String): Response<List<ItemTool>>
}