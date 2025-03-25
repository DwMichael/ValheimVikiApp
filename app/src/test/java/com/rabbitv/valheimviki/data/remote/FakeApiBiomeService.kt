package com.rabbitv.valheimviki.data.remote

import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.domain.model.api_response.ApiResponse
import com.rabbitv.valheimviki.domain.model.biome.Biome

class FakeApiBiomeService : ApiBiomeService {

    private val mockBiomes = listOf(
        Biome(
            id = "biome1-id",
            category = "BIOME",
            imageUrl = "https://example.com/biome1.jpg",
            name = "Temperate Forest",
            description = "A forest characterized by moderate rainfall and distinct seasons.",
            order = 1
        ), Biome(
            id = "biome2-id",
            category = "BIOME",
            imageUrl = "https://example.com/biome2.png",
            name = "Tropical Rainforest",
            description = "A hot, moist biome found near Earth's equator.",
            order = 2
        )
    )

    override suspend fun getAllBiomes(lang: String): ApiResponse<Biome> {
        return ApiResponse<Biome>(
            success = true,
            error = null,
            message = null,
            data = mockBiomes
        )
    }

}