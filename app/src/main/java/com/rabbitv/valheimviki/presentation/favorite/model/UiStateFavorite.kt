package com.rabbitv.valheimviki.presentation.favorite.model

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class UiStateFavorite(
	val biomes: UiState<List<Biome>> = UiState.Loading(),
	val creatures: UiState<List<Creature>> = UiState.Loading(),
	val weapons: UiState<List<Weapon>> = UiState.Loading(),
	val armors: UiState<List<Armor>> = UiState.Loading(),
	val foods: UiState<List<Food>> = UiState.Loading(),
	val meads: UiState<List<Mead>> = UiState.Loading(),
	val craftingObjects: UiState<List<CraftingObject>> = UiState.Loading(),
	val tools: UiState<List<ItemTool>> = UiState.Loading(),
	val materials: UiState<List<Material>> = UiState.Loading(),
	val buildingMaterials: UiState<List<BuildingMaterial>> = UiState.Loading(),
	val oreDeposits: UiState<List<OreDeposit>> = UiState.Loading(),
	val trees: UiState<List<Tree>> = UiState.Loading(),
	val pointsOfInterest: UiState<List<PointOfInterest>> = UiState.Loading()
)