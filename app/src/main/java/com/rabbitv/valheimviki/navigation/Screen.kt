package com.rabbitv.valheimviki.navigation


import com.rabbitv.valheimviki.utils.Constants.AGGRESSIVE_CREATURE_KEY
import com.rabbitv.valheimviki.utils.Constants.ARMOR_KEY
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.FOOD_CATEGORY_KEY
import com.rabbitv.valheimviki.utils.Constants.FOOD_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MINI_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.NPC_KEY
import com.rabbitv.valheimviki.utils.Constants.PASSIVE_CREATURE_KEY
import com.rabbitv.valheimviki.utils.Constants.WEAPON_KEY
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
	@Serializable
	object Splash : Screen("splash_screen")

	@Serializable
	object Welcome : Screen("welcome_screen")

	@Serializable
	object Home : Screen("home_screen")

	@Serializable
	object Biome : Screen("biome_list_screen")

	@Serializable
	object Boss : Screen("boss_list_screen")

	@Serializable
	object MiniBoss : Screen("mini_list_screen")

	@Serializable
	object MobList : Screen("mob_list_screen")

	@Serializable
	object WeaponList : Screen("weapon_list_screen")

	@Serializable
	object ArmorList : Screen("armor_list_screen")

	@Serializable
	object FoodList : Screen("food_list_screen")

	@Serializable
	object MeadList : Screen("mead_list_screen")

	@Serializable
	object ToolList : Screen("tool_list_screen")

	@Serializable
	object MaterialCategory : Screen("material_category_screen")

	@Serializable
	object MaterialList : Screen("material_list_screen")

	@Serializable
	object BuildingMaterialCategory : Screen("building_material_category_screen")

	@Serializable
	object BuildingMaterialList : Screen("building_material_list_screen")

	@Serializable
	object OreDeposit : Screen("ore_deposit_screen")

	@Serializable
	object Tree : Screen("Tree_screen")

	@Serializable
	object PointOfInterest : Screen("point_of_interest_screen")

	@Serializable
	object BiomeDetail :
		Screen("biome_detail/{$BIOME_ARGUMENT_KEY}") {
		fun passArguments(id: String): String {
			return "biome_detail/$id"
		}
	}

	@Serializable
	object MainBossDetail :
		Screen("mainBoss_screen/{$MAIN_BOSS_ARGUMENT_KEY}") {
		fun passArguments(id: String): String {
			return "mainBoss_screen/$id"
		}
	}

	@Serializable
	object MiniBossDetail :
		Screen("miniBoss_screen/{$MINI_BOSS_ARGUMENT_KEY}") {

		fun passArguments(id: String): String {
			return "miniBoss_screen/$id"
		}
	}


	@Serializable
	object AggressiveCreatureDetail :
		Screen("aggressive_creature_screen/{$AGGRESSIVE_CREATURE_KEY}") {
		fun passAggressiveCreatureId(aggressiveCreatureId: String): String {
			return "aggressive_creature_screen/$aggressiveCreatureId"
		}
	}

	@Serializable
	object PassiveCreatureDetail :
		Screen("passive_creature_screen/{$PASSIVE_CREATURE_KEY}") {
		fun passPassiveCreatureId(passiveCreatureId: String): String {
			return "passive_creature_screen/$passiveCreatureId"
		}
	}

	@Serializable
	object NpcDetail :
		Screen("npc_screen/{$NPC_KEY}") {
		fun passNpcId(npcId: String): String {
			return "npc_screen/$npcId"
		}
	}


	@Serializable
	object WeaponDetail :
		Screen("weapon_screen/{$WEAPON_KEY}") {
		fun passWeaponId(weaponId: String): String {
			return "weapon_screen/$weaponId"
		}
	}

	@Serializable
	object ArmorDetail :
		Screen("armor_screen/{$ARMOR_KEY}") {
		fun passArmorId(armorId: String): String {
			return "armor_screen/$armorId"
		}
	}

	@Serializable
	object FoodDetail :
		Screen("food_screen/{$FOOD_KEY}/{$FOOD_CATEGORY_KEY}") {
		fun passFoodId(foodId: String, foodCategory: String): String {
			return "food_screen/$foodId/$foodCategory"
		}
	}


}
