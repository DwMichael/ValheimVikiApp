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
//    @Serializable
//    object Settings : Screen("settings_screen")
    @Serializable
    object Biome : Screen("biome_screen")
//    object Biome : Screen("biome_screen/{biomeId}")
//    {
//        fun passBiomeId(biomeId: Int):String{
//          return "biome_screen/$biomeId"
//        }
//    }
    @Serializable
    object Creature : Screen("creature_screen")
//    {
//        fun passCreatureId(creatureId: Int):String{
//            return "creature_screen/$creatureId"
//        }
//    }

}