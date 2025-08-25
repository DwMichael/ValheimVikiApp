package com.rabbitv.valheimviki.presentation.detail.ore_deposit.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class OreDepositUiState(
	val oreDeposit: OreDeposit? = null,
	val relatedBiomes: UIState< List<Biome>> = UIState.Loading,
	val relatedMaterials: UIState< List<MaterialDrop>> =  UIState.Loading,
	val relatedTools: UIState<List<ItemTool>> =  UIState.Loading,
	val craftingStation: UIState<List<CraftingObject>> =  UIState.Loading,
	val isFavorite: Boolean = false,
)