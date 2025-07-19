package com.rabbitv.valheimviki.navigation


import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.utils.toAppCategory
import com.rabbitv.valheimviki.utils.toFoodSubCategory
import com.rabbitv.valheimviki.utils.toMeadSubCategory
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationDestination

@Serializable
sealed interface TopLevelDestination : NavigationDestination {
	@Serializable
	data object Splash : TopLevelDestination

	@Serializable
	data object Welcome : TopLevelDestination

//	@Serializable
//	data object Home : TopLevelDestination

	@Serializable
	data object Favorite : TopLevelDestination

	@Serializable
	data object Search : TopLevelDestination

}

@Serializable
sealed interface GridDestination : NavigationDestination {
	@Serializable
	sealed interface WorldDestinations : GridDestination {
		@Serializable
		data object BiomeGrid : WorldDestinations

		@Serializable
		data object OreDepositGrid : WorldDestinations

		@Serializable
		data object TreeGrid : WorldDestinations

	}

	@Serializable
	sealed interface CreatureDestinations : GridDestination {
		@Serializable
		data object BossGrid : CreatureDestinations

		@Serializable
		data object MiniBossGrid : CreatureDestinations
	}

}

@Serializable
sealed interface ListDestination : NavigationDestination {

	@Serializable
	sealed interface WorldDestinations : ListDestination {
		@Serializable
		data object PointOfInterestList : WorldDestinations
	}

	@Serializable
	sealed interface CreatureDestinations : ListDestination {

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

	@Serializable
	data class BiomeDetail(
		val biomeId: String
	) : WorldDetailDestination

	@Serializable
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
		val foodId: String,
		val category: FoodSubCategory
	) : ConsumableDetailDestination

	@Serializable
	data class MeadDetail(
		val meadId: String,
		val category: MeadSubCategory,
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

	@Serializable
	data class WoodDetail(
		val woodMaterialId: String
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

	fun routeToCreature(creatureType: String, itemId: String): CreatureDetailDestination {
		return when (creatureType) {
			"BOSS" -> CreatureDetailDestination.MainBossDetail(mainBossId = itemId)
			"MINI_BOSS" -> CreatureDetailDestination.MiniBossDetail(miniBossId = itemId)
			"AGGRESSIVE_CREATURE" -> CreatureDetailDestination.AggressiveCreatureDetail(
				aggressiveCreatureId = itemId
			)

			"PASSIVE_CREATURE" -> CreatureDetailDestination.PassiveCreatureDetail(passiveCreatureId = itemId)
			"NPC" -> CreatureDetailDestination.NpcDetail(npcId = itemId)
			else -> throw IllegalArgumentException("Unknown creature type: $creatureType")
		}
	}

	fun routeToMaterial(materialType: String, itemId: String): MaterialDetailDestination {
		return when (materialType) {
			"BOSS_DROP" -> MaterialDetailDestination.BossDropDetail(bossDropId = itemId)
			"MINI_BOSS_DROP" -> MaterialDetailDestination.MiniBossDropDetail(miniBossDropId = itemId)
			"CREATURE_DROP" -> MaterialDetailDestination.MobDropDetail(mobDropId = itemId)
			"CRAFTED" -> MaterialDetailDestination.CraftedMaterialDetail(craftedMaterialId = itemId)
			"METAL" -> MaterialDetailDestination.MetalMaterialDetail(metalMaterialId = itemId)
			"MISCELLANEOUS" -> MaterialDetailDestination.GeneralMaterialDetail(generalMaterialId = itemId)
			"GEMSTONE" -> MaterialDetailDestination.GemstoneDetail(gemstoneId = itemId)
			"SEED" -> MaterialDetailDestination.SeedDetail(seedMaterialId = itemId)
			"SHOP" -> MaterialDetailDestination.ShopMaterialDetail(shopMaterialId = itemId)
			"VALUABLE" -> MaterialDetailDestination.ValuableDetail(valuableMaterialId = itemId)
			"FORSAKEN_ALTAR_OFFERING" -> MaterialDetailDestination.OfferingsDetail(
				offeringsMaterialId = itemId
			)

			"WOOD" -> MaterialDetailDestination.WoodDetail(woodMaterialId = itemId)

			else -> throw IllegalArgumentException("Unknown material type: $materialType")
		}
	}


	fun routeToDetailScreen(
		itemData: ItemData,
		appCategory: AppCategory
	): DetailDestination {
		return when (appCategory) {
			AppCategory.BIOME -> WorldDetailDestination.BiomeDetail(biomeId = itemData.id)

			AppCategory.CREATURE -> {
				val subCategory = itemData.subCategory
				requireNotNull(subCategory) {
					"Creature items must have a subCategory"
				}
				routeToCreature(
					creatureType = subCategory,
					itemId = itemData.id
				)
			}

			AppCategory.FOOD -> ConsumableDetailDestination.FoodDetail(
				foodId = itemData.id,
				category = itemData.subCategory.toFoodSubCategory()
			)

			AppCategory.ARMOR -> EquipmentDetailDestination.ArmorDetail(armorId = itemData.id)
			AppCategory.WEAPON -> EquipmentDetailDestination.WeaponDetail(weaponId = itemData.id)

			AppCategory.BUILDING_MATERIAL -> BuildingDetailDestination.BuildingMaterialDetail(
				buildingMaterialId = itemData.id
			)

			AppCategory.MATERIAL -> {
				val subCategory = itemData.subCategory
				requireNotNull(subCategory) {
					"Material items must have a subCategory"
				}
				routeToMaterial(
					materialType = subCategory,
					itemId = itemData.id
				)
			}

			AppCategory.CRAFTING -> BuildingDetailDestination.CraftingObjectDetail(itemData.id)
			AppCategory.TOOL -> EquipmentDetailDestination.ToolDetail(itemData.id)

			AppCategory.MEAD -> ConsumableDetailDestination.MeadDetail(
				meadId = itemData.id,
				category = itemData.subCategory.toMeadSubCategory()
			)

			AppCategory.POINTOFINTEREST -> WorldDetailDestination.PointOfInterestDetail(itemData.id)
			AppCategory.TREE -> WorldDetailDestination.TreeDetail(itemData.id)
			AppCategory.OREDEPOSITE -> WorldDetailDestination.OreDepositDetail(itemData.id)
		}
	}

	fun createItemDetailClickHandler(onItemClick: (destination: DetailDestination) -> Unit): (ItemData) -> Unit {
		return { itemData: ItemData ->
			val destination = routeToDetailScreen(
				itemData = itemData,
				appCategory = itemData.category.toAppCategory()
			)
			onItemClick(destination)
		}
	}
}