package com.rabbitv.valheimviki.domain.use_cases.data_refetch

/**
 * Configuration for data refetch operations
 * Contains expected minimum sizes for each data category
 */
object DataRefetchConfig {

	/**
	 * Expected minimum sizes for each data category
	 * These values should be updated when the API data structure changes
	 */
	val EXPECTED_DATA_SIZES = mapOf(
		"biomes" to 9,
		"creatures" to 86,
		"oreDeposits" to 14,
		"materials" to 280,
		"pointsOfInterest" to 51,
		"trees" to 8,
		"food" to 85,
		"weapons" to 117,
		"armors" to 57,
		"meads" to 41,
		"tools" to 14,
		"trinkets" to 13,
		"buildingMaterials" to 261,
		"craftingObjects" to 46,
		"relations" to 2886
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
