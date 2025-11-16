package com.rabbitv.valheimviki.domain.use_cases.data_refetch

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
	const val MIN_SIZE_CREATURES = 83
	const val MIN_SIZE_ORE_DEPOSITS = 14
	const val MIN_SIZE_MATERIALS = 272
	const val MIN_SIZE_POINTS_OF_INTEREST = 49
	const val MIN_SIZE_TREES = 8
	const val MIN_SIZE_FOOD = 84
	const val MIN_SIZE_WEAPONS = 99
	const val MIN_SIZE_ARMORS = 51
	const val MIN_SIZE_MEADS = 41
	const val MIN_SIZE_TOOLS = 14
	const val MIN_SIZE_TRINKETS = 14
	const val MIN_SIZE_BUILDING_MATERIALS = 258
	const val MIN_SIZE_CRAFTING_OBJECTS = 46
	const val MIN_SIZE_RELATIONS = 2886

	/**
	 * Expected minimum sizes for each data category
	 * These values should be updated when the API data structure changes
	 */
	val EXPECTED_DATA_SIZES = mapOf(
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
