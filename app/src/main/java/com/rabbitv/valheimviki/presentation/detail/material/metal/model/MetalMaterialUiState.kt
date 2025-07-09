package com.rabbitv.valheimviki.presentation.detail.material.metal.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.presentation.detail.material.model.MaterialToCraft

data class MetalMaterialUiState(
	val material: Material? = null,
	val biomes: List<Biome> = emptyList(),
	val creatures: List<Creature> = emptyList(),
	val craftingStations: List<CraftingObject> = emptyList(),
	val pointOfInterests: List<PointOfInterest> = emptyList(),
	val oreDeposits: List<OreDeposit> = emptyList(),
	val requiredMaterials: List<MaterialToCraft> = emptyList(),
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null,
)
