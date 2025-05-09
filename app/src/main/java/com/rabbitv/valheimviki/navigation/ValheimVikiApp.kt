@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.rabbitv.valheimviki.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MountainSnow
import com.composables.icons.lucide.Rabbit
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.biome.BiomeScreen
import com.rabbitv.valheimviki.presentation.components.DrawerItem
import com.rabbitv.valheimviki.presentation.components.NavigationDrawer
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossScreen
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossScreen
import com.rabbitv.valheimviki.presentation.creatures.mob_list.MobListScreen
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.AggressiveCreatureDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.MainBossDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.MiniBossDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.PassiveCreatureDetailScreen
import com.rabbitv.valheimviki.presentation.home.MainAppBar
import com.rabbitv.valheimviki.presentation.intro.WelcomeScreen
import com.rabbitv.valheimviki.presentation.splash.SplashScreen
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.Constants.AGGRESSIVE_CREATURE_KEY
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MINI_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.PASSIVE_CREATURE_KEY
import com.rabbitv.valheimviki.utils.Constants.TEXT_ARGUMENT_KEY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun ValheimVikiApp() {
    ValheimVikiAppTheme {
        val valheimVikiNavController = rememberNavController()
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                MainContainer(
                    valheimVikiNavController,
                )
            }
        }
    }
}

@Composable
fun MainContainer(
    valheimVikiNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = getDrawerItems()
    val selectedItem: MutableState<DrawerItem> = remember { mutableStateOf(items[0]) }
    val navBackStackEntry by valheimVikiNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

//    BackHandler {
//
//    }

    val showTopAppBar = currentRoute?.let { route ->
        route.startsWith(Screen.Boss.route.substringBefore("{")) ||
                route.startsWith(Screen.Biome.route.substringBefore("{")) ||
                route.startsWith(Screen.MiniBoss.route.substringBefore("{")) ||
                route.startsWith(Screen.MobList.route.substringBefore("{"))
    } == true

    val isNavigating = remember { mutableStateOf(false) }
    val topBarVisible = remember { mutableStateOf(showTopAppBar) }
    LaunchedEffect(currentRoute) {
        items.find { it.route == currentRoute }?.let {
            selectedItem.value = it
        }
        if (!showTopAppBar) {
            isNavigating.value = true
            delay(250)
            topBarVisible.value = false
            isNavigating.value = false
        } else {
            topBarVisible.value = true
        }
    }


    NavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        scope = scope,
        childNavController = valheimVikiNavController,
        items = items,
        selectedItem = selectedItem,
        isDetailScreen = !showTopAppBar,
    ) {
        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = topBarVisible.value,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
//                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(durationMillis = 200)
                    ) + fadeOut(animationSpec = tween(durationMillis = 200))
                ) {
                    MainAppBar(
                        onSearchBarClick = {},
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onBookMarkClick = {},
                        scope = scope,
                        drawerState = drawerState
                    )
                }
            },
            content = { innerPadding ->
                Image(
                    painter = painterResource(id = R.drawable.main_background),
                    contentDescription = "BackgroundImage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                NavHost(
                    navController = valheimVikiNavController,
                    startDestination = Screen.Splash.route,
                ) {
                    composable(route = Screen.Splash.route) {
                        SplashScreen(valheimVikiNavController)
                    }
                    composable(route = Screen.Welcome.route) {
                        WelcomeScreen(valheimVikiNavController)
                    }
                    //Drawer Screens
                    composable(Screen.Biome.route) {
                        BiomeScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { itemId, text ->
                                valheimVikiNavController.navigate(
                                    Screen.BiomeDetail.passBiomeIdAndText(itemId, text)
                                )
                            },
                            paddingValues = innerPadding,
                            animatedVisibilityScope = this@composable
                        )
                    }

                    composable(Screen.Boss.route) {
                        BossScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { mainBossId, text ->
                                valheimVikiNavController.navigate(
                                    Screen.MainBossDetail.passCreatureId(mainBossId, text)
                                )
                            },
                            paddingValues = innerPadding,
                            animatedVisibilityScope = this@composable
                        )
                    }
                    composable(Screen.MiniBoss.route) {
                        MiniBossScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { miniBossId, text ->
                                valheimVikiNavController.navigate(
                                    Screen.MiniBossDetail.passMiniBossId(miniBossId, text)
                                )
                            },
                            paddingValues = innerPadding,
                            animatedVisibilityScope = this@composable
                        )
                    }
                    composable(Screen.MobList.route) {
                        MobListScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { creatureId, creatureSubCategory ->
                                when (creatureSubCategory) {
                                    0 -> valheimVikiNavController.navigate(
                                        Screen.PassiveCreatureDetail.passPassiveCreatureId(
                                            creatureId
                                        )
                                    )

                                    1 -> valheimVikiNavController.navigate(
                                        Screen.AggressiveCreatureDetail.passAggressiveCreatureId(
                                            creatureId
                                        )
                                    )

                                    2 -> {}
                                }

                            },
                            paddingValues = innerPadding,
//                            animatedVisibilityScope = this@composable
                        )
                    }
                    //Detail Screens
                    composable(
                        Screen.BiomeDetail.route,
                        arguments = listOf(
                            navArgument(BIOME_ARGUMENT_KEY) { type = NavType.StringType },
                            navArgument(TEXT_ARGUMENT_KEY) { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        BiomeDetailScreen(
                            onBack = {
                                valheimVikiNavController.popBackStack()
                            },
                            animatedVisibilityScope = this@composable
                        )
                    }
                    composable(
                        route = Screen.MainBossDetail.route,
                        arguments = listOf(
                            navArgument(MAIN_BOSS_ARGUMENT_KEY) { type = NavType.StringType },
                            navArgument(TEXT_ARGUMENT_KEY) { type = NavType.StringType }
                        )
                    ) {
                        MainBossDetailScreen(
                            onBack = {
                                valheimVikiNavController.popBackStack()
                            },
                            animatedVisibilityScope = this@composable
                        )
                    }
                    composable(
                        route = Screen.MiniBossDetail.route,
                        arguments = listOf(
                            navArgument(MINI_BOSS_ARGUMENT_KEY) { type = NavType.StringType },
                            navArgument(TEXT_ARGUMENT_KEY) { type = NavType.StringType }
                        )
                    ) {
                        MiniBossDetailScreen(
                            onBack = {
                                valheimVikiNavController.popBackStack()
                            },
                            animatedVisibilityScope = this@composable
                        )
                    }
                    composable(
                        route = Screen.AggressiveCreatureDetail.route,
                        arguments = listOf(
                            navArgument(AGGRESSIVE_CREATURE_KEY) { type = NavType.StringType },
                        )
                    ) {
                        AggressiveCreatureDetailScreen(
                            onBack = {
                                valheimVikiNavController.popBackStack()
                            },
                        )
                    }
                    composable(
                        route = Screen.PassiveCreatureDetail.route,
                        arguments = listOf(
                            navArgument(PASSIVE_CREATURE_KEY) { type = NavType.StringType },
                        )
                    ) {
                        PassiveCreatureDetailScreen(
                            onBack = {
                                valheimVikiNavController.popBackStack()
                            },
                        )
                    }
                }
            }
        )
    }
}


@Composable
private fun getDrawerItems(): List<DrawerItem> {
    return listOf(
        DrawerItem(
            icon = Lucide.MountainSnow,
            label = stringResource(R.string.biomes),
            contentDescription = stringResource(R.string.biomes_section),
            route = Screen.Biome.route
        ),
        DrawerItem(
            iconPainter = painterResource(R.drawable.skull),
            label = stringResource(R.string.bosses),
            contentDescription = stringResource(R.string.boss_section),
            route = Screen.Boss.route
        ),
        DrawerItem(
            iconPainter = painterResource(R.drawable.ogre),
            label = stringResource(R.string.minibosses),
            contentDescription = stringResource(R.string.minibosses_section),
            route = Screen.MiniBoss.route
        ),
        DrawerItem(
            icon = Lucide.Rabbit,
            label = "Creatures",
            contentDescription = "Creatures section",
            route = Screen.MobList.route
        ),
    )
}


val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }