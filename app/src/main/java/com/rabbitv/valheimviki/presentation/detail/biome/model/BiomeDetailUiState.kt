package com.rabbitv.valheimviki.presentation.detail.biome.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class BiomeDetailUiState(
	val biome: Biome? = null,
	val mainBoss: UIState<MainBoss?> = UIState.Loading,
	val relatedCreatures: UIState<List<Creature>> = UIState.Loading,
	val relatedOreDeposits: UIState<List<OreDeposit>> = UIState.Loading,
	val relatedMaterials: UIState<List<Material>> = UIState.Loading,
	val relatedPointOfInterest: UIState<List<PointOfInterest>> = UIState.Loading,
	val relatedTrees: UIState<List<Tree>> = UIState.Loading,
	val isFavorite: Boolean = false,
)