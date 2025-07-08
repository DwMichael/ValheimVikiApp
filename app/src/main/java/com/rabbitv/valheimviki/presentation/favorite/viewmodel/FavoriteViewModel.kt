@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
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
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.presentation.favorite.model.UiStateFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
	private val favoriteUseCases: FavoriteUseCases,
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
) : ViewModel() {

	private val favorites: StateFlow<List<String>> = favoriteUseCases.getAllFavoritesUseCase()
		.map { favorites -> favorites.map { it.itemId } }
		.flowOn(Dispatchers.Default)
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000),
			emptyList()
		)

	@OptIn(ExperimentalCoroutinesApi::class)
	val uiState: StateFlow<UiStateFavorite> = combine(
		// Biomes
		favorites.flatMapLatest { favoriteIds ->
			biomeUseCases.getBiomesByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<Biome>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading biomes")) }
				.flowOn(Dispatchers.IO)
		},

		// Creatures
		favorites.flatMapLatest { favoriteIds ->
			creatureUseCases.getCreaturesByIds(favoriteIds)
				.map { UiState.Success(it) as UiState<List<Creature>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading creatures")) }
				.flowOn(Dispatchers.IO)
		},

		// Weapons
		favorites.flatMapLatest { favoriteIds ->
			weaponUseCases.getWeaponsByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<Weapon>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading weapons")) }
				.flowOn(Dispatchers.IO)
		},

		// Armors
		favorites.flatMapLatest { favoriteIds ->
			armorUseCases.getArmorsByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<Armor>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading armors")) }
				.flowOn(Dispatchers.IO)
		},

		// Foods
		favorites.flatMapLatest { favoriteIds ->
			foodUseCases.getFoodListByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<Food>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading foods")) }
				.flowOn(Dispatchers.IO)
		},

		// Meads
		favorites.flatMapLatest { favoriteIds ->
			meadUseCases.getMeadsByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<Mead>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading meads")) }
				.flowOn(Dispatchers.IO)
		},

		// Crafting Objects
		favorites.flatMapLatest { favoriteIds ->
			craftingObjectUseCases.getCraftingObjectsByIds(favoriteIds)
				.map { UiState.Success(it) as UiState<List<CraftingObject>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading crafting objects")) }
				.flowOn(Dispatchers.IO)
		},

		// Tools
		favorites.flatMapLatest { favoriteIds ->
			toolUseCases.getToolsByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<ItemTool>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading tools")) }
				.flowOn(Dispatchers.IO)
		},

		// Materials
		favorites.flatMapLatest { favoriteIds ->
			materialUseCases.getMaterialsByIds(favoriteIds)
				.map { UiState.Success(it) as UiState<List<Material>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading materials")) }
				.flowOn(Dispatchers.IO)
		},

		// Building Materials
		favorites.flatMapLatest { favoriteIds ->
			buildingMaterialUseCases.getBuildMaterialByIds(favoriteIds)
				.map { UiState.Success(it) as UiState<List<BuildingMaterial>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading building materials")) }
				.flowOn(Dispatchers.IO)
		},

		// Ore Deposits
		favorites.flatMapLatest { favoriteIds ->
			oreDepositUseCases.getOreDepositsByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<OreDeposit>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading ore deposits")) }
				.flowOn(Dispatchers.IO)
		},

		// Trees
		favorites.flatMapLatest { favoriteIds ->
			treeUseCases.getTreesByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<Tree>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading trees")) }
				.flowOn(Dispatchers.IO)
		},

		// Points of Interest
		favorites.flatMapLatest { favoriteIds ->
			pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(favoriteIds)
				.map { UiState.Success(it) as UiState<List<PointOfInterest>> }
				.catch { emit(UiState.Error(it.message ?: "Error loading points of interest")) }
				.flowOn(Dispatchers.IO)
		}
	) { values ->
		UiStateFavorite(
			biomes = values[0] as UiState<List<Biome>>,
			creatures = values[1] as UiState<List<Creature>>,
			weapons = values[2] as UiState<List<Weapon>>,
			armors = values[3] as UiState<List<Armor>>,
			foods = values[4] as UiState<List<Food>>,
			meads = values[5] as UiState<List<Mead>>,
			craftingObjects = values[6] as UiState<List<CraftingObject>>,
			tools = values[7] as UiState<List<ItemTool>>,
			materials = values[8] as UiState<List<Material>>,
			buildingMaterials = values[9] as UiState<List<BuildingMaterial>>,
			oreDeposits = values[10] as UiState<List<OreDeposit>>,
			trees = values[11] as UiState<List<Tree>>,
			pointsOfInterest = values[12] as UiState<List<PointOfInterest>>
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		UiStateFavorite()
	)

	fun removeFavorite(favorite: Favorite) {
		viewModelScope.launch {
			favoriteUseCases.deleteFavoriteUseCase(favorite)
		}
	}
}

