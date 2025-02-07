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
import com.rabbitv.valheimviki.presentation.biome.BiomeGridScreen
import com.rabbitv.valheimviki.presentation.biome.CreatureListScreen
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossGirdScreen
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossGridScreen
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeScreen
import com.rabbitv.valheimviki.presentation.detail.creature.CreatureScreen
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.CREATURE_ARGUMENT_KEY

@Composable
fun ChildNavGraph(
    paddingValues: PaddingValues,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.BiomeList.route,
        modifier = Modifier.padding(0.dp)
    ) {
        composable(Screen.BiomeList.route) {
            BiomeGridScreen(
                paddingValues = paddingValues,
                navController = navHostController
            )
        }
        composable(
            route = Screen.Biome.route,
            arguments = listOf(navArgument(BIOME_ARGUMENT_KEY)
            { type = NavType.StringType }
            )
        ) {
            BiomeScreen(
                paddingValues = paddingValues,
            )
        }
        composable(Screen.Boss.route) {
            BossGirdScreen(
                paddingValues = paddingValues,
                navController = navHostController
            )
        }
        composable(Screen.MiniBoss.route) {
            MiniBossGridScreen(
                paddingValues = paddingValues,
                navController = navHostController
            )
        }
        composable(Screen.CreatureList.route) {
            CreatureListScreen(
                paddingValues = paddingValues,
                navController = navHostController
            )
        }
        composable(
            route = Screen.Creature.route,
            arguments = listOf(navArgument(CREATURE_ARGUMENT_KEY)
            { type = NavType.StringType }
            )
        ) {
            CreatureScreen(
                paddingValues = paddingValues,
            )
        }


    }
}