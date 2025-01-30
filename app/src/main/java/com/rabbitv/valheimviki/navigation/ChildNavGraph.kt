package com.rabbitv.valheimviki.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rabbitv.valheimviki.presentation.biome.BiomeScreen
import com.rabbitv.valheimviki.presentation.biome.CreatureScreen

@Composable
fun ChildNavGraph(navHostController: NavHostController,paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Biome.route,
        modifier = Modifier.padding(0.dp)
    ) {
        composable(Screen.Biome.route) {
            BiomeScreen(
                modifier = Modifier,
                contentPadding = paddingValues
            )
        }
        composable(Screen.Creature.route) {
            CreatureScreen()
        }

    }
}