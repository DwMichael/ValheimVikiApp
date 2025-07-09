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
import com.rabbitv.valheimviki.domain.model.item_tool.tool_upgrade_info.ToolsUpgradeInfo
import com.rabbitv.valheimviki.domain.model.weapon.UpgradeInfo
import com.rabbitv.valheimviki.presentation.components.card.GridLevelInfo
import com.rabbitv.valheimviki.presentation.detail.armor.model.StatArmorVisuals
import com.rabbitv.valheimviki.presentation.detail.tool.model.StatToolsVisuals
import com.rabbitv.valheimviki.presentation.detail.weapon.model.StatWeaponVisuals
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteCategory
import retrofit2.Response

fun <T> Response<List<T>>.bodyList(): List<T> {
	return body() ?: emptyList()
}

fun String?.valid() =
	takeIf { !isNullOrBlank() && !equals("null", ignoreCase = true) }

fun String?.toFavoriteCategory(): FavoriteCategory {
	return when(this) {
		"BIOME" -> FavoriteCategory.BIOME
		"CREATURE" -> FavoriteCategory.CREATURE
		"FOOD" -> FavoriteCategory.FOOD
		"ARMOR" -> FavoriteCategory.ARMOR
		"WEAPON" -> FavoriteCategory.WEAPON
		"BUILDING_MATERIAL" -> FavoriteCategory.BUILDING_MATERIAL
		"MATERIAL" -> FavoriteCategory.MATERIAL
		"CRAFTING" -> FavoriteCategory.CRAFTING
		"TOOL" -> FavoriteCategory.TOOL
		"MEAD" -> FavoriteCategory.MEAD
		"POINTOFINTEREST" -> FavoriteCategory.POINTOFINTEREST
		"TREE" -> FavoriteCategory.TREE
		"OREDEPOSITE" -> FavoriteCategory.OREDEPOSITE
		else -> error("Unknown Favorite Category: $this")
	}
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