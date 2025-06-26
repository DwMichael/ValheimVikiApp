package com.rabbitv.valheimviki.presentation.detail.tool.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit

data class ToolDetailUiState(
	val tool: ItemTool? = null,
	val relatedCraftingStation: CraftingObject? = null,
	val relatedMaterials: List<MaterialUpgrade> = emptyList(),
	val relatedOreDeposits: List<OreDeposit> = emptyList(),
	val relatedNpc: Creature? = null,
	val isLoading: Boolean = false,
	val error: String? = null
)