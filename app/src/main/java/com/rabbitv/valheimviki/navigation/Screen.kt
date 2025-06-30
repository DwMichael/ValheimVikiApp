package com.rabbitv.valheimviki.navigation


import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
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
	data object BiomeList : Screen()

	@Serializable
	data object BossList : Screen()

	@Serializable
	data object MiniBossList : Screen()

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
	data object CraftingObjectsList : Screen()

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
	data object OreDepositList : Screen()

	@Serializable
	data object TreeGrid : Screen()

	@Serializable
	data object PointOfInterestList : Screen()

	//graphs
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
		val foodCategory: FoodSubCategory
	) : Screen()

	@Serializable
	data class MeadDetail(
		val meadId: String,
		val meadCategory: MeadSubCategory
	) : Screen()

	@Serializable
	data class CraftingObjectDetail(val craftingObjectId: String) : Screen()

	@Serializable
	data class ToolDetail(val toolId: String) : Screen()

	@Serializable
	data class TreeDetail(val treeId: String) : Screen()

	@Serializable
	data class PointOfInterestDetail(val pointOfInterestId: String) : Screen()

	@Serializable
	data class OreDepositDetail(val oreDepositId: String) : Screen()


	//Materials Details Screens
	@Serializable
	data class BossDropDetail(val bossDropId: String) : Screen()

	@Serializable
	data class CraftedMaterialDetail(val craftedMaterialId: String) : Screen()

	@Serializable
	data class GeneralMaterialDetail(val generalMaterialId: String) : Screen()

	@Serializable
	data class MetalMaterialDetail(val metalMaterialId: String) : Screen()

	@Serializable
	data class MiniBossDropDetail(val miniBossDropId: String) : Screen()

	@Serializable
	data class MobDropDetail(val mobDropId: String) : Screen()

	@Serializable
	data class OfferingsDetail(val offeringsMaterialId: String) : Screen()

	@Serializable
	data class GemstoneDetail(val gemstoneId: String) : Screen()

	@Serializable
	data class SeedDetail(val seedMaterialId: String) : Screen()

	@Serializable
	data class ShopMaterialDetail(val shopMaterialId: String) : Screen()

	@Serializable
	data class ValuableDetail(val valuableMaterialId: String) : Screen()

	//BuildingDetail
	@Serializable
	data class BuildingMaterialDetail(val buildingMaterialId: String) : Screen()
}
