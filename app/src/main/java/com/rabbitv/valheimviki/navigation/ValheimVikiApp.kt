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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.FlaskRound
import com.composables.icons.lucide.Gavel
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinned
import com.composables.icons.lucide.MountainSnow
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.Rabbit
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Trees
import com.composables.icons.lucide.Utensils
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.presentation.armor.ArmorListScreen
import com.rabbitv.valheimviki.presentation.biome.BiomeScreen
import com.rabbitv.valheimviki.presentation.building_material.BuildingMaterialCategoryScreen
import com.rabbitv.valheimviki.presentation.building_material.BuildingMaterialListScreen
import com.rabbitv.valheimviki.presentation.building_material.viewmodel.BuildingMaterialListViewModel
import com.rabbitv.valheimviki.presentation.components.DrawerItem
import com.rabbitv.valheimviki.presentation.components.NavigationDrawer
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossScreen
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossScreen
import com.rabbitv.valheimviki.presentation.creatures.mob_list.MobListScreen
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.AggressiveCreatureDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.MainBossDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.MiniBossDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.npc.NpcDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.PassiveCreatureDetailScreen
import com.rabbitv.valheimviki.presentation.food.FoodListScreen
import com.rabbitv.valheimviki.presentation.home.MainAppBar
import com.rabbitv.valheimviki.presentation.intro.WelcomeScreen
import com.rabbitv.valheimviki.presentation.material.MaterialCategoryScreen
import com.rabbitv.valheimviki.presentation.material.MaterialListScreen
import com.rabbitv.valheimviki.presentation.material.viewmodel.MaterialListViewModel
import com.rabbitv.valheimviki.presentation.mead.MeadListScreen
import com.rabbitv.valheimviki.presentation.ore_deposit.OreDepositScreen
import com.rabbitv.valheimviki.presentation.points_of_interest.PoiListScreen
import com.rabbitv.valheimviki.presentation.splash.SplashScreen
import com.rabbitv.valheimviki.presentation.tool.ToolListScreen
import com.rabbitv.valheimviki.presentation.tree.TreeScreen
import com.rabbitv.valheimviki.presentation.weapons.WeaponListScreen
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.Constants.AGGRESSIVE_CREATURE_KEY
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MINI_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.NPC_KEY
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

    val showTopAppBar = currentRoute?.let { route ->
        route.startsWith(Screen.Boss.route.substringBefore("{")) ||
                route.startsWith(Screen.Biome.route.substringBefore("{")) ||
                route.startsWith(Screen.MiniBoss.route.substringBefore("{")) ||
                route.startsWith(Screen.MobList.route.substringBefore("{")) ||
                route.startsWith(Screen.WeaponList.route.substringBefore("{")) ||
                route.startsWith(Screen.ArmorList.route.substringBefore("{")) ||
                route.startsWith(Screen.FoodList.route.substringBefore("{")) ||
                route.startsWith(Screen.MeadList.route.substringBefore("{")) ||
                route.startsWith(Screen.ToolList.route.substringBefore("{")) ||
                route.startsWith(Screen.MaterialCategory.route.substringBefore("{")) ||
                route.startsWith(Screen.BuildingMaterialCategory.route.substringBefore("{")) ||
                route.startsWith(Screen.OreDeposit.route.substringBefore("{")) ||
                route.startsWith(Screen.Tree.route.substringBefore("{")) ||
                route.startsWith(Screen.PointOfInterest.route.substringBefore("{"))
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
                            onItemClick = { creatureId, creatureSubCategory: CreatureSubCategory ->
                                when (creatureSubCategory) {
                                    CreatureSubCategory.PASSIVE_CREATURE -> valheimVikiNavController.navigate(
                                        Screen.PassiveCreatureDetail.passPassiveCreatureId(
                                            creatureId
                                        )
                                    )

                                    CreatureSubCategory.AGGRESSIVE_CREATURE -> valheimVikiNavController.navigate(
                                        Screen.AggressiveCreatureDetail.passAggressiveCreatureId(
                                            creatureId
                                        )
                                    )

                                    CreatureSubCategory.NPC -> valheimVikiNavController.navigate(
                                        Screen.NpcDetail.passNpcId(
                                            creatureId
                                        )
                                    )

                                    CreatureSubCategory.BOSS -> null
                                    CreatureSubCategory.MINI_BOSS -> null
                                }
                            },
                            paddingValues = innerPadding,
//                            animatedVisibilityScope = this@composable
                        )
                    }

                    composable(Screen.WeaponList.route) {
                        WeaponListScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = {},
                            paddingValues = innerPadding,
                        )
                    }

                    composable(Screen.ArmorList.route) {
                        ArmorListScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = {},
                            paddingValues = innerPadding,
                        )
                    }
                    composable(Screen.FoodList.route) {
                        FoodListScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { foodId, foodSubCategory: FoodSubCategory ->
                                when (foodSubCategory) {
                                    FoodSubCategory.UNCOOKED_FOOD -> null
                                    FoodSubCategory.COOKED_FOOD -> null
                                }
                            },
                            paddingValues = innerPadding,
                        )
                    }

                    composable(Screen.MeadList.route) {
                        MeadListScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { meadId, meadSubCategory: MeadSubCategory ->
                                when (meadSubCategory) {
                                    MeadSubCategory.MEAD_BASE -> null
                                    MeadSubCategory.POTION -> null
                                }
                            },
                            paddingValues = innerPadding,
                        )
                    }
                    composable(Screen.ToolList.route) {
                        ToolListScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { toolId, _ ->
                            },
                            paddingValues = innerPadding,
                        )
                    }

                    navigation(
                        startDestination = Screen.MaterialCategory.route,
                        route = "material_graph"    // <-- group them under one graph
                    ) {
                        composable(Screen.MaterialCategory.route) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                valheimVikiNavController.getBackStackEntry("material_graph")
                            }
                            val vm = hiltViewModel<MaterialListViewModel>(parentEntry)
                            MaterialCategoryScreen(
                                modifier = Modifier.padding(10.dp),
                                paddingValues = innerPadding,
                                onGridCategoryClick = {
                                    valheimVikiNavController.navigate(Screen.MaterialList.route)
                                },
                                viewModel = vm
                            )
                        }

                        composable(Screen.MaterialList.route) { backStackEntry ->

                            val parentEntry = remember(backStackEntry) {
                                valheimVikiNavController.getBackStackEntry("material_graph")
                            }
                            val vm = hiltViewModel<MaterialListViewModel>(parentEntry)
                            MaterialListScreen(
                                onItemClick = { materialId, _ ->
                                    {}
                                },
                                onBackClick = {
                                    valheimVikiNavController.popBackStack()
                                },
                                viewModel = vm
                            )
                        }
                    }
                    navigation(
                        startDestination = Screen.BuildingMaterialCategory.route,
                        route = "building_material_graph"
                    ) {
                        composable(Screen.BuildingMaterialCategory.route) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                valheimVikiNavController.getBackStackEntry("building_material_graph")
                            }
                            val vm = hiltViewModel<BuildingMaterialListViewModel>(parentEntry)
                            BuildingMaterialCategoryScreen(
                                modifier = Modifier.padding(10.dp),
                                paddingValues = innerPadding,
                                onGridCategoryClick = {
                                    valheimVikiNavController.navigate(Screen.BuildingMaterialList.route)
                                },
                                viewModel = vm
                            )
                        }

                        composable(Screen.BuildingMaterialList.route) { backStackEntry ->

                            val parentEntry = remember(backStackEntry) {
                                valheimVikiNavController.getBackStackEntry("building_material_graph")
                            }
                            val vm = hiltViewModel<BuildingMaterialListViewModel>(parentEntry)
                            BuildingMaterialListScreen(
                                onItemClick = { buildingMaterialId, _ ->
                                    {}
                                },
                                onBackClick = {
                                    valheimVikiNavController.popBackStack()
                                },
                                viewModel = vm
                            )
                        }
                    }

                    composable(Screen.OreDeposit.route) {
                        OreDepositScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { itemId, text ->
//                                valheimVikiNavController.navigate(
//                                    Screen.BiomeDetail.passBiomeIdAndText(itemId, text)
//                                )
                            },
                            paddingValues = innerPadding,
                            animatedVisibilityScope = this@composable
                        )
                    }
                    composable(Screen.Tree.route) {
                        TreeScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { itemId, text ->
//                                valheimVikiNavController.navigate(
//                                    Screen.BiomeDetail.passBiomeIdAndText(itemId, text)
//                                )
                            },
                            paddingValues = innerPadding,
                            animatedVisibilityScope = this@composable
                        )
                    }
                    composable(Screen.PointOfInterest.route) {
                        PoiListScreen(
                            modifier = Modifier.padding(10.dp),
                            onItemClick = { poiId, poiSubCategory: PointOfInterestSubCategory ->
                                when (poiSubCategory) {
                                    PointOfInterestSubCategory.FORSAKEN_ALTAR -> null
                                    PointOfInterestSubCategory.STRUCTURE -> null
                                }
                            },
                            paddingValues = innerPadding,
                        )
                    }
                    //Detail Screens
                    composable(
                        Screen.BiomeDetail.route,
                        arguments = listOf(
                            navArgument(BIOME_ARGUMENT_KEY) { type = NavType.StringType },
                            navArgument(TEXT_ARGUMENT_KEY) { type = NavType.StringType },

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
                    composable(
                        route = Screen.NpcDetail.route,
                        arguments = listOf(
                            navArgument(NPC_KEY) { type = NavType.StringType },
                        )
                    ) {
                        NpcDetailScreen(
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
        DrawerItem(
            icon = Lucide.Swords,
            label = "Weapons",
            contentDescription = "Weapons section",
            route = Screen.WeaponList.route
        ),
        DrawerItem(
            icon = Lucide.Shield,
            label = "Armors",
            contentDescription = "Armor section",
            route = Screen.ArmorList.route
        ),
        DrawerItem(
            icon = Lucide.Utensils,
            label = "Food",
            contentDescription = "Food section",
            route = Screen.FoodList.route
        ),
        DrawerItem(
            icon = Lucide.FlaskRound,
            label = "Meads",
            contentDescription = "Mead section",
            route = Screen.MeadList.route
        ),
        DrawerItem(
            icon = Lucide.Gavel,
            label = "Tools",
            contentDescription = "Tools section",
            route = Screen.ToolList.route
        ),
        DrawerItem(
            icon = Lucide.Cuboid,
            label = "Materials",
            contentDescription = "Materials section",
            route = Screen.MaterialCategory.route
        ),
        DrawerItem(
            icon = Lucide.House,
            label = "Building Materials",
            contentDescription = "Building Materials section",
            route = Screen.BuildingMaterialCategory.route
        ),
        DrawerItem(
            icon = Lucide.Pickaxe,
            label = "Ore Deposits",
            contentDescription = "Ore Deposits section",
            route = Screen.OreDeposit.route
        ),
        DrawerItem(
            icon = Lucide.Trees,
            label = "Trees",
            contentDescription = "Trees section",
            route = Screen.Tree.route
        ),
        DrawerItem(
            icon = Lucide.MapPinned,
            label = "Points Of Interest",
            contentDescription = "Points Of Interest section",
            route = Screen.PointOfInterest.route
        )
    )
}


val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }