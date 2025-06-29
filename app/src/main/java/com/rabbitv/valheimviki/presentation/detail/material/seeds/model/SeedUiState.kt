package com.rabbitv.valheimviki.presentation.detail.material.seeds.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class SeedUiState(
	val material: Material? = null,
	val biomes: List<Biome> = emptyList(),
	val pointOfInterest: List<PointOfInterest> = emptyList(),
	val tool: ItemTool? = null,
	val isLoading: Boolean = false,
	val error: String? = null,
)
