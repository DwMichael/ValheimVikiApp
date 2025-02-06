package com.rabbitv.valheimviki.navigation

import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.CREATURE_ARGUMENT_KEY

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Welcome : Screen("welcome_screen")

    @Serializable
    object Second : Screen("second_screen")

    @Serializable
    object Home : Screen("home_screen")

    @Serializable
    object BiomeList : Screen("biome_list_screen")

    @Serializable
    object CreatureList : Screen("creature_list_screen")

    @Serializable
    object Boss : Screen("boss_screen")

    @Serializable
    object MiniBoss : Screen("mini_boss_screen/")

    @Serializable
    object Creature : Screen("creature_screen/{$CREATURE_ARGUMENT_KEY}") {
        fun passCreatureId(creatureId: String): String {
            return "creature_screen/$creatureId"
        }
    }

    @Serializable
    object Biome : Screen("biome_screen/{$BIOME_ARGUMENT_KEY}") {
        fun passBiomeId(biomeId: String): String {
            return "biome_screen/$biomeId"
        }
    }

}