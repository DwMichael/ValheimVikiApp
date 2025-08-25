package com.rabbitv.valheimviki.presentation.detail.tool.model

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.upgrader.MaterialUpgrade

data class ToolDetailUiState(
	val tool: ItemTool? = null,
	val relatedCraftingStation: UIState< CraftingObject?> = UIState.Loading,
	val relatedMaterials: UIState< List<MaterialUpgrade>> = UIState.Loading,
	val relatedOreDeposits: UIState< List<OreDeposit>> = UIState.Loading,
	val relatedNpc: UIState<List<Creature>> = UIState.Loading,
	val isFavorite: Boolean = false,
)