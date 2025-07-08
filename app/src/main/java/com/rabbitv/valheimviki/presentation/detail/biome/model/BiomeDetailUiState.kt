package com.rabbitv.valheimviki.presentation.detail.biome.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree

data class BiomeDetailUiState(
	val biome: Biome? = null,
	val mainBoss: MainBoss? = null,
	val relatedCreatures: List<Creature> = emptyList(),
	val relatedOreDeposits: List<OreDeposit> = emptyList(),
	val relatedMaterials: List<Material> = emptyList(),
	val relatedPointOfInterest: List<PointOfInterest> = emptyList(),
	val relatedTrees: List<Tree> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)