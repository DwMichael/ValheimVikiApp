package com.rabbitv.valheimviki.presentation.detail.material.seeds.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree

data class SeedUiState(
	val material: Material? = null,
	val biomes: List<Biome> = emptyList(),
	val pointsOfInterest: List<PointOfInterest> = emptyList(),
	val npc: NPC? = null,
	val tools: List<ItemTool> = emptyList(),
	val trees: List<Tree> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null,
)
