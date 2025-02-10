package com.rabbitv.valheimviki.data.remote

import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.domain.model.biome.BiomeDto
import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX

class FakeApiBiomeService : ApiBiomeService {

    private val mockBiomes = listOf(
        BiomeDtoX(
            id = "biome1-id",
            stage = "Stage 1",
            imageUrl = "https://example.com/biome1.jpg",
            name = "Temperate Forest",
            description = "A forest characterized by moderate rainfall and distinct seasons.",
            order = 1
        ), BiomeDtoX(
            id = "biome2-id",
            stage = "Stage 2",
            imageUrl = "https://example.com/biome2.png",
            name = "Tropical Rainforest",
            description = "A hot, moist biome found near Earth's equator.",
            order = 2
        )
    )

    override suspend fun getAllBiomes(lang: String): BiomeDto {
        return BiomeDto(
            success = true,
            error = null,
            errorDetails = null,
            biomes = mockBiomes
        )
    }

}