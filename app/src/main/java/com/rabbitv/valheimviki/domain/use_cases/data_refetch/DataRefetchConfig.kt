package com.rabbitv.valheimviki.domain.use_cases.data_refetch

import com.rabbitv.valheimviki.BuildConfig

/**
 * Configuration for data refetch operations
 * Contains expected minimum sizes for each data category
 */
object DataRefetchConfig {
	// Category name constants
	const val CATEGORY_BIOMES = "biomes"
	const val CATEGORY_CREATURES = "creatures"
	const val CATEGORY_ORE_DEPOSITS = "oreDeposits"
	const val CATEGORY_MATERIALS = "materials"
	const val CATEGORY_POINTS_OF_INTEREST = "pointsOfInterest"
	const val CATEGORY_TREES = "trees"
	const val CATEGORY_FOOD = "food"
	const val CATEGORY_WEAPONS = "weapons"
	const val CATEGORY_ARMORS = "armors"
	const val CATEGORY_MEADS = "meads"
	const val CATEGORY_TOOLS = "tools"
	const val CATEGORY_TRINKETS = "trinkets"
	const val CATEGORY_BUILDING_MATERIALS = "buildingMaterials"
	const val CATEGORY_CRAFTING_OBJECTS = "craftingObjects"
	const val CATEGORY_RELATIONS = "relations"

	// Expected minimum size constants
	const val MIN_SIZE_BIOMES = 9
	const val MIN_SIZE_CREATURES = 86
	const val MIN_SIZE_ORE_DEPOSITS = 14
	const val MIN_SIZE_MATERIALS = 280
	const val MIN_SIZE_POINTS_OF_INTEREST = 51
	const val MIN_SIZE_TREES = 8
	const val MIN_SIZE_FOOD = 85
	const val MIN_SIZE_WEAPONS = 117
	const val MIN_SIZE_ARMORS = 57
	const val MIN_SIZE_MEADS = 41
	const val MIN_SIZE_TOOLS = 14
	const val MIN_SIZE_TRINKETS = 13
	const val MIN_SIZE_BUILDING_MATERIALS = 261
	const val MIN_SIZE_CRAFTING_OBJECTS = 46
	const val MIN_SIZE_RELATIONS = 2886

	private val REMOTE_EXPECTED_DATA_SIZES = mapOf(
		CATEGORY_BIOMES to MIN_SIZE_BIOMES,
		CATEGORY_CREATURES to MIN_SIZE_CREATURES,
		CATEGORY_ORE_DEPOSITS to MIN_SIZE_ORE_DEPOSITS,
		CATEGORY_MATERIALS to MIN_SIZE_MATERIALS,
		CATEGORY_POINTS_OF_INTEREST to MIN_SIZE_POINTS_OF_INTEREST,
		CATEGORY_TREES to MIN_SIZE_TREES,
		CATEGORY_FOOD to MIN_SIZE_FOOD,
		CATEGORY_WEAPONS to MIN_SIZE_WEAPONS,
		CATEGORY_ARMORS to MIN_SIZE_ARMORS,
		CATEGORY_MEADS to MIN_SIZE_MEADS,
		CATEGORY_TOOLS to MIN_SIZE_TOOLS,
		CATEGORY_TRINKETS to MIN_SIZE_TRINKETS,
		CATEGORY_BUILDING_MATERIALS to MIN_SIZE_BUILDING_MATERIALS,
		CATEGORY_CRAFTING_OBJECTS to MIN_SIZE_CRAFTING_OBJECTS,
		CATEGORY_RELATIONS to MIN_SIZE_RELATIONS
	)

	private val LOCAL_EXPECTED_DATA_SIZES = mapOf(
		CATEGORY_BIOMES to 9,
		CATEGORY_CREATURES to 83,
		CATEGORY_ORE_DEPOSITS to 9,
		CATEGORY_MATERIALS to 270,
		CATEGORY_POINTS_OF_INTEREST to 51,
		CATEGORY_TREES to 8,
		CATEGORY_FOOD to 84,
		CATEGORY_WEAPONS to 100,
		CATEGORY_ARMORS to 51,
		CATEGORY_MEADS to 40,
		CATEGORY_TOOLS to 14,
		CATEGORY_TRINKETS to 0,
		CATEGORY_BUILDING_MATERIALS to 248,
		CATEGORY_CRAFTING_OBJECTS to 46,
		CATEGORY_RELATIONS to 1273
	)

	/**
	 * Expected minimum sizes for each data category.
	 * Local flavor uses the private dump counts; real flavor keeps production API thresholds.
	 */
	val EXPECTED_DATA_SIZES =
		if (BuildConfig.FLAVOR == "local_app_varian") LOCAL_EXPECTED_DATA_SIZES else REMOTE_EXPECTED_DATA_SIZES

	/**
	 * Categories that should be included in search indexing
	 */
	val SEARCHABLE_CATEGORIES = setOf(
		"BiomeGrid", "BossGrid", "MiniBossGrid", "MobList",
		"WeaponList", "ArmorList", "TrinketList", "FoodList", "MeadList",
		"CraftingObjectsList", "ToolList", "MaterialCategory",
		"BuildingMaterialCategory", "OreDepositGrid", "TreeGrid", "PointOfInterestList"
	)

	/**
	 * Maximum number of retry attempts for failed API calls
	 */
	const val MAX_RETRY_ATTEMPTS = 3

	/**
	 * Timeout for individual API calls in milliseconds
	 */
	const val API_TIMEOUT_MS = 30000L
}
