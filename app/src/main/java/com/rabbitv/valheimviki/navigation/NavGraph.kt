package com.rabbitv.valheimviki.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rabbitv.valheimviki.presentation.HomeScreen
import com.rabbitv.valheimviki.presentation.biome.BiomeScreen
import com.rabbitv.valheimviki.presentation.biome.CreatureScreen
import com.rabbitv.valheimviki.presentation.core.SecondScreen
import com.rabbitv.valheimviki.presentation.core.WelcomeScreen


@Composable
fun SetupNavGraph(navController: NavHostController){



    NavHost(
        navController = navController,
        startDestination = Screen.Creature.route
        ){
        composable(
            route = Screen.Welcome.route
        ){
            WelcomeScreen()
        }
        composable(
            route = Screen.Second.route
        ){
            SecondScreen()
        }
        composable(
            route = Screen.Home.route
        ){
            HomeScreen()
        }
        composable(
            route = Screen.Biome.route,
//            arguments = listOf(navArgument(name = DETAILS_BIOME_ARGUMENT_KEY){
//                type = NavType.StringType
//                }
//            )
        ){
            BiomeScreen()
        }
        composable(
            route = Screen.Creature.route,
//            arguments = listOf(navArgument(name = DETAILS_CREATURE_ARGUMENT_KEY){
//                type = NavType.StringType
//            }
//                    )
        ){
            CreatureScreen()
        }
//        composable(
//            route = Screen.Settings.route
//        ){
//            SettingsScreen()
//        }

    }

}