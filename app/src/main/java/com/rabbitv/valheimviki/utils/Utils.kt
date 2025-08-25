package com.rabbitv.valheimviki.utils

import com.composables.icons.lucide.Anvil
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Beaker
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Gavel
import com.composables.icons.lucide.Ghost
import com.composables.icons.lucide.Hammer
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.ShieldCheck
import com.composables.icons.lucide.ShieldPlus
import com.composables.icons.lucide.Skull
import com.composables.icons.lucide.Snowflake
import com.composables.icons.lucide.Sparkles
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Sword
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Target
import com.composables.icons.lucide.Trees
import com.composables.icons.lucide.Zap
import com.rabbitv.valheimviki.domain.model.armor.UpgradeArmorInfo
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.item_tool.tool_upgrade_info.ToolsUpgradeInfo
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.weapon.UpgradeInfo
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.presentation.components.card.GridLevelInfo
import com.rabbitv.valheimviki.presentation.detail.armor.model.StatArmorVisuals
import com.rabbitv.valheimviki.presentation.detail.tool.model.StatToolsVisuals
import com.rabbitv.valheimviki.presentation.detail.weapon.model.StatWeaponVisuals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
fun <E, K : Comparable<K>> relatedListFlowGated(
	idsFlow: Flow<List<String>>,
	contentStart: Flow<Boolean>,
	fetcher: (List<String>) -> Flow<List<E>>,
	sortBy: ((E) -> K)? = null
): Flow<UIState<List<E>>> =
	contentStart.flatMapLatest { active ->
		if (!active) {
			flowOf(UIState.Loading)
		} else {
			idsFlow.flatMapLatest { ids ->
				val source: Flow<List<E>> = fetcher(ids)
				val sortedFlow = if (sortBy != null) {
					source.map { list -> list.sortedBy(sortBy) }
				} else {
					source
				}
				sortedFlow
					.distinctUntilChanged()
					.map<List<E>, UIState<List<E>>> { UIState.Success(it) }
					.catch { e -> emit(UIState.Error(e.message ?: "Error")) }
			}
		}
	}


@OptIn(ExperimentalCoroutinesApi::class)
fun <T> relatedDataFlow(
	idsFlow: Flow<List<String>>,
	fetcher: (List<String>) -> Flow<T>
): Flow<UIState<T>> =
	idsFlow.flatMapLatest { ids ->
		fetcher(ids)
			.map<T, UIState<T>> { UIState.Success(it) }
			.catch { e -> emit(UIState.Error(e.message ?: "Unknown error")) }
	}

@OptIn(ExperimentalCoroutinesApi::class)
fun <Id, Raw, Ui> Flow<List<Id>>.toUiState(
	fetcher: (List<Id>) -> Flow<Raw>,
	mapper: (Raw) -> Ui,
	errorMsg: String = "Unknown error"
): Flow<UIState<Ui>> =
	flatMapLatest { ids ->
		fetcher(ids)
			.map<Raw, UIState<Ui>> { UIState.Success(mapper(it)) }
			.catch { e -> emit(UIState.Error(e.message ?: errorMsg)) }
	}


fun String?.valid() =
	takeIf { !isNullOrBlank() && !equals("null", ignoreCase = true) }

fun String?.toAppCategory(): AppCategory {
	return when (this) {
		"BIOME" -> AppCategory.BIOME
		"CREATURE" -> AppCategory.CREATURE
		"FOOD" -> AppCategory.FOOD
		"ARMOR" -> AppCategory.ARMOR
		"WEAPON" -> AppCategory.WEAPON
		"BUILDING_MATERIAL" -> AppCategory.BUILDING_MATERIAL
		"MATERIAL" -> AppCategory.MATERIAL
		"CRAFTING" -> AppCategory.CRAFTING
		"TOOL" -> AppCategory.TOOL
		"MEAD" -> AppCategory.MEAD
		"POINTOFINTEREST" -> AppCategory.POINTOFINTEREST
		"TREE" -> AppCategory.TREE
		"OREDEPOSITE" -> AppCategory.OREDEPOSITE
		else -> error("Unknown Favorite Category: $this")
	}
}

fun String?.toMeadSubCategory(): MeadSubCategory {
	return when (this) {
		"MEAD_BASE" -> MeadSubCategory.MEAD_BASE
		"POTION" -> MeadSubCategory.POTION
		else -> throw IllegalArgumentException("Unknown category: $this")
	}
}

fun String?.toFoodSubCategory(): FoodSubCategory {
	return when (this) {
		"UNCOOKED_FOOD" -> FoodSubCategory.UNCOOKED_FOOD
		"COOKED_FOOD" -> FoodSubCategory.COOKED_FOOD
		else -> throw IllegalArgumentException("Unknown category: $this")
	}
}

fun ItemData.inferDropType(): DroppableType = when (this) {
	is Food -> DroppableType.FOOD
	is Mead -> DroppableType.MEAD
	is Material -> DroppableType.MATERIAL
	else        -> DroppableType.MATERIAL
}
fun mapUpgradeInfoToGridList(upgradeInfo: UpgradeInfo): List<GridLevelInfo> {
	val gridList = mutableListOf<GridLevelInfo>()
	var currentId = 1

	upgradeInfo.upgradeLevels?.let { power ->
		val visual = StatWeaponVisuals.QUALITY
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Star,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.fireDamage?.let { power ->
		val visual = StatWeaponVisuals.FIRE_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Flame,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.frostDamage?.let { power ->
		val visual = StatWeaponVisuals.FROST_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Snowflake,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.slashDamage?.let { power ->
		val visual = StatWeaponVisuals.SLASH_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Swords,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.spiritDamage?.let { power ->
		val visual = StatWeaponVisuals.SPIRIT_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Ghost,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.durability?.let { power ->
		val visual = StatWeaponVisuals.DURABILITY
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Shield,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.stationLevel?.let { power ->
		val visual = StatWeaponVisuals.STATION_LEVEL
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Anvil,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.lightningDamage?.let { power ->
		val visual = StatWeaponVisuals.LIGHTNING_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Zap,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.pierceDamage?.let { power ->
		val visual = StatWeaponVisuals.PIERCE_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Target,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.bluntDamage?.let { power ->
		val visual = StatWeaponVisuals.BLUNT_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Gavel,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.poisonDamage?.let { power ->
		val visual = StatWeaponVisuals.POISON_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Beaker,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.chopDamage?.let { power ->
		val visual = StatWeaponVisuals.CHOP_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Axe,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.pureDamage?.let { power ->
		val visual = StatWeaponVisuals.PURE_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Sparkles,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.pickaxeDamage?.let { power ->
		val visual = StatWeaponVisuals.PICKAXE_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Pickaxe,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.damageAbsorbedBloodMagic0?.let { power ->
		val visual = StatWeaponVisuals.DAMAGE_ABSORBED_BLOOD_MAGIC_0
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.ShieldCheck,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.maximumSkeletonsControllable?.let { power ->
		val visual = StatWeaponVisuals.MAXIMUM_SKELETONS
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Skull,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.damageAbsorbedBloodMagic100?.let { power ->
		val visual = StatWeaponVisuals.DAMAGE_ABSORBED_BLOOD_MAGIC_100
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.ShieldPlus,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}
	upgradeInfo.chopTreesDamage?.let { power ->
		val visual = StatWeaponVisuals.CHOP_TREES
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Trees,
				visual.colorHex,
				visual.displayName,
				power.toInt()
			)
		)
	}

	return gridList
}


fun mapUpgradeArmorInfoToGridList(upgradeArmorInfo: UpgradeArmorInfo): List<GridLevelInfo> {
	val gridList = mutableListOf<GridLevelInfo>()
	var currentId = 1

	upgradeArmorInfo.upgradeLevel?.let { power ->
		val visual = StatArmorVisuals.QUALITY
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Star,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}

	upgradeArmorInfo.armor?.let { power ->
		val visual = StatArmorVisuals.ARMOR
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.ShieldCheck,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}

	upgradeArmorInfo.durability?.let { power ->
		val visual = StatArmorVisuals.DURABILITY
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Heart,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}

	upgradeArmorInfo.stationLevel?.let { power ->
		val visual = StatArmorVisuals.STATION_LEVEL
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Hammer,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}


	return gridList
}

fun mapUpgradeToolsInfoToGridList(upgradeToolsInfo: ToolsUpgradeInfo): List<GridLevelInfo> {
	val gridList = mutableListOf<GridLevelInfo>()
	var currentId = 1

	upgradeToolsInfo.pierceDamage?.let { power ->
		val visual = StatToolsVisuals.PIERCE_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Sword,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}

	upgradeToolsInfo.pickaxeDamage?.let { power ->
		val visual = StatToolsVisuals.PICKAXE_DAMAGE
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Pickaxe,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}

	upgradeToolsInfo.quality?.let { power ->
		val visual = StatToolsVisuals.QUALITY
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Star,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}

	upgradeToolsInfo.durability?.let { power ->
		val visual = StatToolsVisuals.DURABILITY
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Heart,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}

	upgradeToolsInfo.stationLevel?.let { power ->
		val visual = StatToolsVisuals.STATION_LEVEL
		gridList.add(
			GridLevelInfo(
				currentId++,
				Lucide.Hammer,
				visual.colorHex,
				visual.displayName,
				power
			)
		)
	}

	return gridList
}