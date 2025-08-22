package com.rabbitv.valheimviki.presentation.detail.material.crafted.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.presentation.detail.material.model.MaterialToCraft
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class CraftedMaterialUiState(
	val material: Material? = null,
	val requiredCraftingStations: UIState<List<CraftingObject>> = UIState.Loading,
	val relatedMaterial: UIState<List<MaterialToCraft>> = UIState.Loading,
	val isFavorite: Boolean = false
)
