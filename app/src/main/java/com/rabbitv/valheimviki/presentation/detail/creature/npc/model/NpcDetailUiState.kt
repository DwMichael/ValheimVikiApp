package com.rabbitv.valheimviki.presentation.detail.creature.npc.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class NpcDetailUiState(
	val npc: NPC? = null,
	val biome: Biome? = null,
	val shopItems: UIState<List<Material>> = UIState.Loading,
	val shopSellItems: UIState<List<Material>> =  UIState.Loading,
	val hildirChests:UIState< List<Material>> =  UIState.Loading,
	val chestsLocation: UIState<List<PointOfInterest>> =  UIState.Loading,
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)