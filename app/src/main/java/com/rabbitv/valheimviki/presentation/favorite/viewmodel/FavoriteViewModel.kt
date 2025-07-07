package com.rabbitv.valheimviki.presentation.favorite.viewmodel

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
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
	private val biomeUseCases: BiomeUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val weaponUseCases: WeaponUseCases,
	private val armorUseCases: ArmorUseCases,
	private val foodUseCases: FoodUseCases,
	private val meadUseCases: MeadUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val toolUseCases: ToolUseCases,
	private val materialUseCases: MaterialUseCases,
	private val buildingMaterialUseCases: BuildMaterialUseCases,
	private val oreDepositUseCases: OreDepositUseCases,
	private val treeUseCases: TreeUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases
) {
	private val biomes = MutableStateFlow<UiState<List<Biome>>>(UiState.Loading())
	private val creatures = MutableStateFlow<UiState<List<Creature>>>(UiState.Loading())
	private val weapons = MutableStateFlow<UiState<List<Weapon>>>(UiState.Loading())
	private val armors = MutableStateFlow<UiState<List<Armor>>>(UiState.Loading())
	private val food = MutableStateFlow<UiState<List<Food>>>(UiState.Loading())
	private val meads = MutableStateFlow<UiState<List<Mead>>>(UiState.Loading())
	private val craftingObjects = MutableStateFlow<UiState<List<CraftingObject>>>(UiState.Loading())
	private val tools = MutableStateFlow<UiState<List<ItemTool>>>(UiState.Loading())
	private val materials = MutableStateFlow<UiState<List<Material>>>(UiState.Loading())
	private val buildingMaterials =
		MutableStateFlow<UiState<List<BuildingMaterial>>>(UiState.Loading())
	private val oreDeposits = MutableStateFlow<UiState<List<OreDeposit>>>(UiState.Loading())
	private val trees = MutableStateFlow<UiState<List<Tree>>>(UiState.Loading())
	private val pointsOfInterest =
		MutableStateFlow<UiState<List<PointOfInterest>>>(UiState.Loading())


}