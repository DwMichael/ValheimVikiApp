package com.rabbitv.valheimviki.presentation.detail.point_of_interest.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class PointOfInterestUiState(
	val pointOfInterest: PointOfInterest? = null,
	val relatedBiomes: List<Biome> = emptyList(),
	val relatedCreatures: List<Creature> = emptyList(),
	val relatedWeapons: List<Weapon> = emptyList(),
	val relatedFoods: List<Food> = emptyList(),
	val relatedOreDeposits: List<OreDeposit> = emptyList(),
	val relatedOfferings: List<Material> = emptyList(),
	val relatedMaterialDrops: List<MaterialDrop> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null,
)
