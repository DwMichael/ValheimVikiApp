package com.rabbitv.valheimviki.utils

import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory

object Constants {


	//Languages
	const val DEFAULT_LANG = "en"
	const val PL_LANG = "pl"

	//DataSource
	const val PREFERENCES_NAME = "valheimViki_preferences"
	const val PREFERENCES_KEY = "on_boarding_completed"
	const val PREFERENCES_LANGUAGE_KEY = "language_saved"

	//GITDSIZE
	const val BIOME_GRID_COLUMNS = 2
	const val NORMAL_SIZE_GRID = 2
	const val LARGE_SIZE_GRID = 3
	const val LAST_ON_BOARDING_PAGE = 2

	//DATA NAVIGATION ARGUMENT KEYS
	const val BIOME_ARGUMENT_KEY = "biomeId"
	const val MAIN_BOSS_ARGUMENT_KEY = "mainBossId"
	const val MINI_BOSS_ARGUMENT_KEY = "miniBossId"
	const val AGGRESSIVE_CREATURE_KEY = "aggressiveCreatureId"
	const val PASSIVE_CREATURE_KEY = "passiveCreatureId"
	const val NPC_KEY = "npcId"
	const val WEAPON_KEY = "weaponId"
	const val ARMOR_KEY = "armorId"
	const val FOOD_KEY = "foodId"
	const val MEAD_KEY = "meadId"
	const val CRAFTING_KEY = "craftingObjectId"
	const val TOOL_KEY = "toolId"
	const val TREE_KEY = "treeId"
	const val POINT_OF_INTEREST_KEY = "pointOfInterestId"
	const val ORE_DEPOSIT_KEY = "oreDepositId"

	const val BOSS_DROP_KEY = "bossDropId"
	const val MINI_BOSS_DROP_KEY = "miniBossDropId"
	const val MOB_DROP_KEY = "mobDropId"
	const val CRAFTED_MATERIAL_KEY = "craftedMaterialId"
	const val GENERAL_MATERIAL_KEY = "generalMaterialId"
	const val METAL_MATERIAL_KEY = "metalMaterialId"


	const val OFFERINGS_MATERIAL_KEY = "offeringsMaterialId"
	const val POINT_OF_INTEREST_MATERIAL_KEY = "gemstoneId"
	const val SEED_MATERIAL_KEY = "seedMaterialId"
	const val SHOP_MATERIAL_KEY = "shopMaterialId"
	const val VALUABLE_MATERIAL_KEY = "valuableMaterialId"

	const val BUILDING_MATERIAL_DETAIL_KEY = "buildingMaterialId"

	//Creature Order map
	val CreatureSubCategory_ORDER_MAP = mapOf(
		CreatureSubCategory.BOSS.toString() to 1,
		CreatureSubCategory.MINI_BOSS.toString() to 2,
		CreatureSubCategory.AGGRESSIVE_CREATURE.toString() to 3,
		CreatureSubCategory.PASSIVE_CREATURE.toString() to 4,
		CreatureSubCategory.NPC.toString() to 5,
	)


}
