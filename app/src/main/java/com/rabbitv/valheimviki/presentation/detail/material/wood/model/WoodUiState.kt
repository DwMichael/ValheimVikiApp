package com.rabbitv.valheimviki.presentation.detail.material.wood.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.tree.Tree

data class WoodUiState(
	val material: Material? = null,
	val biomes: List<Biome> = emptyList(),
	val trees: List<Tree> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null,
)
