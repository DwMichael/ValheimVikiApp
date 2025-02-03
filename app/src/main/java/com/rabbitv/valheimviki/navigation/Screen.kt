package com.rabbitv.valheimviki.navigation

import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY

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
    object BiomeList : Screen("biome_list_screen")


    @Serializable
    object Creature : Screen("creature_screen")


    @Serializable
    object Biome: Screen("biome_screen/{$BIOME_ARGUMENT_KEY}")
    {
        fun passBiomeId(biomeId:String):String{
            return "details_screen/$biomeId"
        }
    }

}