package com.rabbitv.valheimviki.presentation.detail.crafting.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject

data class CraftingDetailUiState(
	val craftingObject: CraftingObject? = null,
	val craftingUpgraderObjects: List<CraftingProducts> = emptyList(),
	val craftingFoodProducts: List<CraftingProducts> = emptyList(),
	val craftingMeadProducts: List<CraftingProducts> = emptyList(),
	val craftingMaterialToBuild: List<CraftingProducts> = emptyList(),
	val craftingMaterialRequired: List<CraftingProducts> = emptyList(),
	val craftingMaterialProducts: List<CraftingProducts> = emptyList(),
	val craftingWeaponProducts: List<CraftingProducts> = emptyList(),
	val craftingArmorProducts: List<CraftingProducts> = emptyList(),
	val craftingToolProducts: List<CraftingProducts> = emptyList(),
	val craftingBuildingMaterialProducts: List<CraftingProducts> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null
)
