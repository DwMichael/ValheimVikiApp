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
import com.rabbitv.valheimviki.presentation.biome.BiomeScreen
import com.rabbitv.valheimviki.presentation.biome.CreatureScreen
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossScreen
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossScreen
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.CreatureDetailScreen
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.CREATURE_ARGUMENT_KEY

@Composable
fun ChildNavGraph(
    paddingValues: PaddingValues,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Biome.route,
        modifier = Modifier.padding(0.dp)
    ) {
        composable(Screen.Biome.route) {
            BiomeScreen(
                paddingValues = paddingValues,
                navController = navHostController
            )
        }
        composable(
            Screen.BiomeDetail.route,
            arguments = listOf(navArgument(BIOME_ARGUMENT_KEY) { type = NavType.StringType })
        ) {
            BiomeDetailScreen(paddingValues = paddingValues)
        }
        composable(Screen.Boss.route) {
            BossScreen(
                paddingValues = paddingValues,
                navController = navHostController
            )
        }
        composable(Screen.MiniBoss.route) {
            MiniBossScreen(
                paddingValues = paddingValues,
                navController = navHostController
            )
        }
        composable(Screen.Creature.route) {
            CreatureScreen(
                paddingValues = paddingValues,
                navController = navHostController
            )
        }
        composable(
            route = Screen.CreatureDetail.route,
            arguments = listOf(navArgument(CREATURE_ARGUMENT_KEY)
            { type = NavType.StringType }
            )
        ) {
            CreatureDetailScreen(
                paddingValues = paddingValues,
            )
        }


    }
}