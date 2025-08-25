package com.rabbitv.valheimviki.presentation.detail.point_of_interest.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class PointOfInterestUiState(
	val pointOfInterest: PointOfInterest? = null,
	val relatedBiomes: UIState<List<Biome>> = UIState.Loading,
	val relatedCreatures: UIState<List<Creature>> = UIState.Loading,
	val relatedWeapons: UIState<List<Weapon>> = UIState.Loading,
	val relatedFoods: UIState<List<Food>> = UIState.Loading,
	val relatedOreDeposits: UIState<List<OreDeposit>> = UIState.Loading,
	val relatedOfferings: UIState<List<Material>> = UIState.Loading,
	val relatedMaterialDrops: UIState<List<MaterialDrop>> = UIState.Loading,
	val isFavorite: Boolean = false,
)
