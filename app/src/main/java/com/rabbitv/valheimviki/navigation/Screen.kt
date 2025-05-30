package com.rabbitv.valheimviki.navigation


import android.net.Uri
import com.rabbitv.valheimviki.utils.Constants.AGGRESSIVE_CREATURE_KEY
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.BIOME_IMAGE_URL_KEY
import com.rabbitv.valheimviki.utils.Constants.BIOME_NAME_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_IMAGE_URL_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_NAME_KEY
import com.rabbitv.valheimviki.utils.Constants.MINI_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MINI_BOSS_IMAGE_URL_KEY
import com.rabbitv.valheimviki.utils.Constants.MINI_BOSS_NAME_KEY
import com.rabbitv.valheimviki.utils.Constants.NPC_KEY
import com.rabbitv.valheimviki.utils.Constants.PASSIVE_CREATURE_KEY
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
        Screen("biome_detail/{$BIOME_ARGUMENT_KEY}/{$BIOME_IMAGE_URL_KEY}/{$BIOME_NAME_KEY}") {
        fun passArguments(id: String, imageUrl: String, name: String): String {
            return "biome_detail/$id/${Uri.encode(imageUrl)}/${Uri.encode(name)}"
        }
    }

    @Serializable
    object MainBossDetail :
        Screen("mainBoss_screen/{$MAIN_BOSS_ARGUMENT_KEY}/{$MAIN_BOSS_IMAGE_URL_KEY}/{$MAIN_BOSS_NAME_KEY}") {
        fun passArguments(id: String, imageUrl: String, name: String): String {
            return "mainBoss_screen/$id/${Uri.encode(imageUrl)}/${Uri.encode(name)}"
        }
    }

    @Serializable
    object MiniBossDetail :
        Screen("miniBoss_screen/{$MINI_BOSS_ARGUMENT_KEY}/{$MINI_BOSS_IMAGE_URL_KEY}/{$MINI_BOSS_NAME_KEY}") {
        
        fun passArguments(id: String, imageUrl: String, name: String): String {
            return "miniBoss_screen/$id/${Uri.encode(imageUrl)}/${Uri.encode(name)}"
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


}