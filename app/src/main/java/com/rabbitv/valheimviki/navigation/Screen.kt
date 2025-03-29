package com.rabbitv.valheimviki.navigation

import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY


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
    object Creature : Screen("creature_list_screen")

    @Serializable
    object Boss : Screen("boss_screen")

    @Serializable
    object MiniBoss : Screen("mini_boss_screen/")

    @Serializable
    object CreatureDetail : Screen("creature_screen/{$MAIN_BOSS_ARGUMENT_KEY}") {
        fun passCreatureId(mainBossId: String): String {
            return "creature_screen/$mainBossId"
        }
    }

    @Serializable
    object BiomeDetail : Screen("biome_screen/{$BIOME_ARGUMENT_KEY}") {
        fun passBiomeId(biomeId: String): String {
            return "biome_screen/$biomeId"
        }
    }

}