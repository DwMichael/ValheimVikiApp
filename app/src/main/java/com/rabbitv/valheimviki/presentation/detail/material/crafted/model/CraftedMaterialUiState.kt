package com.rabbitv.valheimviki.presentation.detail.material.crafted.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.presentation.detail.material.model.MaterialToCraft

data class CraftedMaterialUiState(
	val material: Material? = null,
	val requiredCraftingStations: List<CraftingObject> = emptyList(),
	val relatedMaterial: List<MaterialToCraft> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null,
)
