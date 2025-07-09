package com.rabbitv.valheimviki.presentation.detail.ore_deposit.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit

data class OreDepositUiState(
	val oreDeposit: OreDeposit? = null,
	val relatedBiomes: List<Biome> = emptyList(),
	val relatedMaterials: List<MaterialDrop> = emptyList(),
	val relatedTools: List<ItemTool> = emptyList(),
	val craftingStation: List<CraftingObject> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)