package com.rabbitv.valheimviki.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.navigation.navigation
import com.rabbitv.valheimviki.presentation.biome.BiomeScreen
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossScreen
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossScreen
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.CreatureDetailScreen
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.DETAIL_ROUTE_GRAPH
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_ROUTE_GRAPH
import com.rabbitv.valheimviki.utils.Constants.TEXT_ARGUMENT_KEY

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChildNavGraph(
    sharedTransitionScope :SharedTransitionScope,
    paddingValues: PaddingValues,
    navHostController: NavHostController
) {
        NavHost(
            navController = navHostController,
            startDestination = MAIN_ROUTE_GRAPH,
            modifier = Modifier.padding(0.dp)
        ) {
            navigation(
                startDestination = Screen.Biome.route,
                route = MAIN_ROUTE_GRAPH
            ) {
                composable(Screen.Biome.route) {
                        BiomeScreen(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = this,
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { itemId, text ->
                                navHostController.navigate(
                                    Screen.BiomeDetail.passBiomeIdAndText(itemId, text)
                                )
                            },
                            paddingValues = paddingValues
                        )
                }

                composable(Screen.Boss.route) {
                        BossScreen(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = this,
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { itemId, text ->
                                navHostController.navigate(
                                 Screen.CreatureDetail.passCreatureId(itemId, text)
                                )
                            },
                            paddingValues = paddingValues
                        )
                }
                composable(Screen.MiniBoss.route) {
                        MiniBossScreen(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = this,
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { itemId, text ->
                                navHostController.navigate(
                                    Screen.CreatureDetail.passCreatureId(itemId, text)
                                )
                            },
                            paddingValues = paddingValues
                        )
                }
            }

            navigation(
                startDestination = Screen.BiomeDetail.route,
                route = DETAIL_ROUTE_GRAPH,
            ) {
                composable(
                    Screen.BiomeDetail.route,
                    arguments = listOf(
                        navArgument(BIOME_ARGUMENT_KEY) { type = NavType.StringType },
                        navArgument(TEXT_ARGUMENT_KEY) { type = NavType.StringType }
                    )
                ) {backStackEntry ->


                    BiomeDetailScreen(
                        onBack = { navHostController.popBackStack() },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = this,
                        paddingValues = paddingValues
                    )
                }
                composable(
                    route = Screen.CreatureDetail.route,
                    arguments = listOf(
                        navArgument(MAIN_BOSS_ARGUMENT_KEY) { type = NavType.StringType },
                        navArgument(TEXT_ARGUMENT_KEY) { type = NavType.StringType }
                    )
                ) {
                    CreatureDetailScreen(
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = this,
                        paddingValues = paddingValues,
                    )
                }
            }
        }

}
