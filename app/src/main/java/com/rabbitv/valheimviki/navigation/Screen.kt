package com.rabbitv.valheimviki.navigation


import kotlinx.serialization.Serializable

@Serializable
sealed class Screen() {
	@Serializable
	data object Splash : Screen()

	@Serializable
	data object Welcome : Screen()

	@Serializable
	data object Home : Screen()

	@Serializable
	data object Biome : Screen()

	@Serializable
	data object Boss : Screen()

	@Serializable
	data object MiniBoss : Screen()

	@Serializable
	data object MobList : Screen()

	@Serializable
	data object WeaponList : Screen()

	@Serializable
	data object ArmorList : Screen()

	@Serializable
	data object FoodList : Screen()

	@Serializable
	data object MeadList : Screen()

	@Serializable
	data object ToolList : Screen()

	@Serializable
	data object MaterialCategory : Screen()

	@Serializable
	data object MaterialList : Screen()

	@Serializable
	data object BuildingMaterialCategory : Screen()

	@Serializable
	data object BuildingMaterialList : Screen()

	@Serializable
	data object OreDeposit : Screen()

	@Serializable
	data object Tree : Screen()

	@Serializable
	data object PointOfInterest : Screen()

	@Serializable
	data object MaterialGraph : Screen()

	@Serializable
	data object BuildingMaterialsGraph : Screen()

	@Serializable
	data class BiomeDetail(
		val biomeId: String
	)

	@Serializable
	data class MainBossDetail(
		val mainBossId: String
	)

	@Serializable
	data class MiniBossDetail(
		val miniBossId: String
	)

	@Serializable
	data class AggressiveCreatureDetail(
		val aggressiveCreatureId: String
	)

	@Serializable
	data class PassiveCreatureDetail(
		val passiveCreatureId: String
	)

	@Serializable
	data class NpcDetail(
		val npcId: String
	)

	@Serializable
	data class WeaponDetail(
		val weaponId: String
	)

	@Serializable
	data class ArmorDetail(
		val armorId: String
	)

	@Serializable
	data class FoodDetail(
		val foodId: String,
		val foodCategory: String
	) : Screen()
}
