package com.rabbitv.valheimviki.presentation.detail.material.shop.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material

data class ShopUiState(
	val material: Material? = null,
	val biome: Biome? = null,
	val npc: List<Creature> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null,
)
