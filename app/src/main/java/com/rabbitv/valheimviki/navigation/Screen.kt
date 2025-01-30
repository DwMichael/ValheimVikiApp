package com.rabbitv.valheimviki.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed class Screen(val route:String) {
    @Serializable
    object Welcome : Screen("welcome_screen")
    @Serializable
    object Second : Screen("second_screen")
    @Serializable
    object Home : Screen("home_screen")

    @Serializable
    object Biome : Screen("biome_screen")

    @Serializable
    object Creature : Screen("creature_screen")


}