package com.rabbitv.valheimviki.navigation


import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationDestination

@Serializable
sealed interface TopLevelDestination : NavigationDestination {
	@Serializable
	data object Splash : TopLevelDestination

	@Serializable
	data object Welcome : TopLevelDestination

	@Serializable
	data object Home : TopLevelDestination
}


@Serializable
sealed interface ListDestination : NavigationDestination {

	@Serializable
	sealed interface WorldDestinations : ListDestination {
		@Serializable
		data object BiomeList : WorldDestinations

		@Serializable
		data object OreDepositList : WorldDestinations

		@Serializable
		data object TreeGrid : WorldDestinations

		@Serializable
		data object PointOfInterestList : WorldDestinations
	}

	@Serializable
	sealed interface CreatureDestinations : ListDestination {
		@Serializable
		data object BossList : CreatureDestinations

		@Serializable
		data object MiniBossList : CreatureDestinations

		@Serializable
		data object MobList : CreatureDestinations
	}

	@Serializable
	sealed interface ItemDestinations : ListDestination {
		@Serializable
		data object WeaponList : ItemDestinations

		@Serializable
		data object ArmorList : ItemDestinations

		@Serializable
		data object ToolList : ItemDestinations
	}

	@Serializable
	sealed interface FoodDestinations : ListDestination {
		@Serializable
		data object FoodList : ItemDestinations

		@Serializable
		data object MeadList : ItemDestinations
	}

	@Serializable
	sealed interface CraftingDestinations : ListDestination {
		@Serializable
		data object CraftingObjectsList : CraftingDestinations

		@Serializable
		data object MaterialCategory : CraftingDestinations

		@Serializable
		data object MaterialList : CraftingDestinations

		@Serializable
		data object BuildingMaterialCategory : CraftingDestinations

		@Serializable
		data object BuildingMaterialList : CraftingDestinations
	}
}

@Serializable
sealed interface DetailDestination : NavigationDestination

@Serializable
sealed interface WorldDetailDestination : DetailDestination {
	data class BiomeDetail(
		val biomeId: String
	) : WorldDetailDestination

	data class OreDepositDetail(
		val oreDepositId: String
	) : WorldDetailDestination

	@Serializable
	data class TreeDetail(
		val treeId: String
	) : WorldDetailDestination

	@Serializable
	data class PointOfInterestDetail(
		val pointOfInterestId: String
	) : WorldDetailDestination
}

// Creature-related details with subcategories
@Serializable
sealed interface CreatureDetailDestination : DetailDestination {
	@Serializable
	data class MainBossDetail(
		val mainBossId: String
	) : CreatureDetailDestination

	@Serializable
	data class MiniBossDetail(
		val miniBossId: String
	) : CreatureDetailDestination

	@Serializable
	data class AggressiveCreatureDetail(
		val aggressiveCreatureId: String
	) : CreatureDetailDestination

	@Serializable
	data class PassiveCreatureDetail(
		val passiveCreatureId: String
	) : CreatureDetailDestination

	@Serializable
	data class NpcDetail(
		val npcId: String
	) : CreatureDetailDestination
}

// Equipment details
@Serializable
sealed interface EquipmentDetailDestination : DetailDestination {
	@Serializable
	data class WeaponDetail(
		val weaponId: String
	) : EquipmentDetailDestination

	@Serializable
	data class ArmorDetail(
		val armorId: String
	) : EquipmentDetailDestination

	@Serializable
	data class ToolDetail(
		val toolId: String
	) : EquipmentDetailDestination
}

// Consumable details with category information
@Serializable
sealed interface ConsumableDetailDestination : DetailDestination {
	@Serializable
	data class FoodDetail(
		val itemId: String,
		val foodId: String
	) : ConsumableDetailDestination

	@Serializable
	data class MeadDetail(
		val meadId: String,
		val categoryId: String
	) : ConsumableDetailDestination
}

// Material details with multiple types
@Serializable
sealed interface MaterialDetailDestination : DetailDestination {
	@Serializable
	data class BossDropDetail(
		val bossDropId: String
	) : MaterialDetailDestination

	@Serializable
	data class MiniBossDropDetail(
		val miniBossDropId: String
	) : MaterialDetailDestination

	@Serializable
	data class MobDropDetail(
		val mobDropId: String
	) : MaterialDetailDestination

	@Serializable
	data class CraftedMaterialDetail(
		val craftedMaterialId: String
	) : MaterialDetailDestination

	@Serializable
	data class GeneralMaterialDetail(
		val generalMaterialId: String
	) : MaterialDetailDestination

	@Serializable
	data class MetalMaterialDetail(
		val metalMaterialId: String
	) : MaterialDetailDestination

	@Serializable
	data class OfferingsDetail(
		val offeringsMaterialId: String
	) : MaterialDetailDestination

	@Serializable
	data class GemstoneDetail(
		val gemstoneId: String
	) : MaterialDetailDestination

	@Serializable
	data class SeedDetail(
		val seedMaterialId: String
	) : MaterialDetailDestination

	@Serializable
	data class ShopMaterialDetail(
		val shopMaterialId: String
	) : MaterialDetailDestination

	@Serializable
	data class ValuableDetail(
		val valuableMaterialId: String
	) : MaterialDetailDestination
}

// Building-related details
@Serializable
sealed interface BuildingDetailDestination : DetailDestination {
	@Serializable
	data class BuildingMaterialDetail(
		val buildingMaterialId: String
	) : BuildingDetailDestination

	@Serializable
	data class CraftingObjectDetail(
		val craftingObjectId: String
	) : BuildingDetailDestination
}

// Navigation graphs as separate concept
@Serializable
sealed interface NavigationGraph : NavigationDestination {
	@Serializable
	data object MaterialGraph : NavigationGraph

	@Serializable
	data object BuildingMaterialsGraph : NavigationGraph
}

object NavigationHelper {

	fun foodCategoryToString(category: FoodSubCategory): String = category.toString()
	fun meadCategoryToString(category: MeadSubCategory): String = category.toString()

	fun routeToCreature(creatureType: String, itemId: String): CreatureDetailDestination {
		return when (creatureType) {
			"BOSS" -> CreatureDetailDestination.MainBossDetail(itemId)
			"MINI_BOSS" -> CreatureDetailDestination.MiniBossDetail(itemId)
			"AGGRESSIVE" -> CreatureDetailDestination.AggressiveCreatureDetail(itemId)
			"PASSIVE" -> CreatureDetailDestination.PassiveCreatureDetail(itemId)
			"NPC" -> CreatureDetailDestination.NpcDetail(itemId)
			else -> throw IllegalArgumentException("Unknown creature type: $creatureType")
		}
	}

	fun routeToMaterial(materialType: String, itemId: String): MaterialDetailDestination {
		return when (materialType) {
			"BOSS_DROP" -> MaterialDetailDestination.BossDropDetail(itemId)
			"MINI_BOSS_DROP" -> MaterialDetailDestination.MiniBossDropDetail(itemId)
			"CREATURE_DROP" -> MaterialDetailDestination.MobDropDetail(itemId)
			"CRAFTED" -> MaterialDetailDestination.CraftedMaterialDetail(itemId)
			"METAL" -> MaterialDetailDestination.MetalMaterialDetail(itemId)
			"MISCELLANEOUS" -> MaterialDetailDestination.GeneralMaterialDetail(itemId)
			"GEMSTONE" -> MaterialDetailDestination.GemstoneDetail(itemId)
			"SEED" -> MaterialDetailDestination.SeedDetail(itemId)
			"SHOP" -> MaterialDetailDestination.ShopMaterialDetail(itemId)
			"VALUABLE" -> MaterialDetailDestination.ValuableDetail(itemId)
			"FORSAKEN_ALTAR_OFFERING" -> MaterialDetailDestination.OfferingsDetail(itemId)
			else -> throw IllegalArgumentException("Unknown material type: $materialType")
		}
	}
}