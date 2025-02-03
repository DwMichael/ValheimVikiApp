package com.rabbitv.valheimviki.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rabbitv.valheimviki.presentation.biome.BiomeListScreen
import com.rabbitv.valheimviki.presentation.biome.CreatureScreen
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeScreen
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY

@Composable
fun ChildNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.BiomeList.route,
        modifier = Modifier.padding(0.dp)
    ) {
        composable(Screen.Creature.route) {
        CreatureScreen()
        }
        composable(Screen.BiomeList.route) {
            BiomeListScreen(navController =  navHostController)
        }
        composable(
            route = Screen.Biome.route,
            arguments = listOf(navArgument(BIOME_ARGUMENT_KEY){type = NavType.StringType})
        ) {
            BiomeScreen()
        }

    }
}