package com.rabbitv.valheimviki.presentation.detail.material.general.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class GeneralMaterialUiState(
	val material: Material? = null,
	val biomes: UIState<List<Biome>> = UIState.Loading,
	val craftingStations: UIState<List<CraftingObject>> = UIState.Loading,
	val pointOfInterests: UIState<List<PointOfInterest>> = UIState.Loading,
	val oreDeposits: UIState<List<OreDeposit>> = UIState.Loading,
	val trees: UIState<List<Tree>> = UIState.Loading,
	val isFavorite: Boolean = false
)
