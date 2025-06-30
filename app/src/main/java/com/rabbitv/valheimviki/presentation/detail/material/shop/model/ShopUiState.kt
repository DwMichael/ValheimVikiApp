package com.rabbitv.valheimviki.presentation.detail.material.shop.model

import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material

data class ShopUiState(
	val material: Material? = null,
	val npc: NPC? = null,
	val isLoading: Boolean = false,
	val error: String? = null,
)
