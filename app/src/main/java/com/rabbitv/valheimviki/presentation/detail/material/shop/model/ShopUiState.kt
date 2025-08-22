package com.rabbitv.valheimviki.presentation.detail.material.shop.model

import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class ShopUiState(
	val material: Material? = null,
	val npc: UIState<NPC?> = UIState.Loading,
	val isFavorite: Boolean = false
)
