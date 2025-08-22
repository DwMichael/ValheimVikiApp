package com.rabbitv.valheimviki.presentation.detail.material.seeds.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class SeedUiState(
	val material: Material? = null,
	val biomes: UIState<List<Biome>> = UIState.Loading,
	val pointsOfInterest: UIState<List<PointOfInterest>> = UIState.Loading,
	val npc: UIState<NPC?> = UIState.Loading,
	val tools: UIState<List<ItemTool>> = UIState.Loading,
	val trees: UIState<List<Tree>> = UIState.Loading,
	val isFavorite: Boolean = false
)
