@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.rabbitv.valheimviki.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import com.rabbitv.valheimviki.presentation.detail.weapon.WeaponDetailScreen
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
import com.rabbitv.valheimviki.utils.Constants.ARMOR_KEY
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.MINI_BOSS_ARGUMENT_KEY
import com.rabbitv.valheimviki.utils.Constants.NPC_KEY
import com.rabbitv.valheimviki.utils.Constants.PASSIVE_CREATURE_KEY
import com.rabbitv.valheimviki.utils.Constants.WEAPON_KEY
import kotlinx.coroutines.launch

private val topBarScreens = setOf(
    Screen.Boss,
    Screen.Biome,
    Screen.MiniBoss,
    Screen.MobList,
    Screen.WeaponList,
    Screen.ArmorList,
    Screen.FoodList,
    Screen.MeadList,
    Screen.ToolList,
    Screen.MaterialCategory,
    Screen.BuildingMaterialCategory,
    Screen.OreDeposit,
    Screen.Tree,
    Screen.PointOfInterest,
)


@Preview
@Composable
fun ValheimVikiApp() {
    ValheimVikiAppTheme {
        SharedTransitionLayout {
            val valheimVikiNavController = rememberNavController()
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                MainContainer(
                    valheimVikiNavController
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainContainer(
    valheimVikiNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerItems: List<DrawerItem> = rememberDrawerItems()
    val selectedItem = remember { mutableStateOf(drawerItems.first()) }


    val currentBackStackEntry by valheimVikiNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isAnimationRunning = remember { mutableStateOf(false) }


    BackHandler {
        if (drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        }
    }

    LaunchedEffect(currentRoute) {
        val match = drawerItems.firstOrNull { item ->
            currentRoute?.startsWith(item.route.substringBefore("{")) == true
        }
        selectedItem.value = match ?: drawerItems.first()
    }

    val showTopAppBar = currentRoute?.let { route ->
        topBarScreens.any { screen ->
            route.startsWith(screen.route.substringBefore("{"))
        }
    } == true

    val transitionScope = LocalSharedTransitionScope.current
        ?: error("No SharedTransitionScope")

    val running by remember {
        derivedStateOf { transitionScope.isTransitionActive }
    }


    NavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        scope = scope,
        childNavController = valheimVikiNavController,
        items = drawerItems,
        selectedItem = selectedItem,
        isDetailScreen = showTopAppBar,
        currentRoute = currentRoute,
        isTransitionActive = running,
    ) {
        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = showTopAppBar,
                    enter = slideInVertically { -it },
                    exit = slideOutVertically { -it } + fadeOut(),
                ) {
                    MainAppBar(
                        scope = scope,
                        drawerState = drawerState,
                        enabled = running
                    )
                }
            },
        ) { innerPadding ->
            val targetTopPadding = if (showTopAppBar) innerPadding.calculateTopPadding() else 0.dp
            val animatedTopPadding by animateDpAsState(
                targetValue = targetTopPadding,
                animationSpec = tween(durationMillis = 450, easing = LinearOutSlowInEasing)
            )

            Box(
                modifier = Modifier
                    .padding(top = animatedTopPadding)
                    .fillMaxSize()

            ) {
                ValheimNavGraph(
                    valheimVikiNavController = valheimVikiNavController,
                    innerPadding = PaddingValues(0.dp),
                    isAnimationRunning = isAnimationRunning
                )
            }
        }
    }
}

@Composable
fun ValheimNavGraph(
    valheimVikiNavController: NavHostController,
    innerPadding: PaddingValues,
    isAnimationRunning: MutableState<Boolean>
) {
    val lastClickTime = remember { mutableLongStateOf(0L) }
    val clickDebounceMillis = 500

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

        composable(Screen.Biome.route) {
            BiomeScreen(
                modifier = Modifier.padding(10.dp),
                onItemClick = { id ->
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
                        lastClickTime.longValue = currentTime
                        valheimVikiNavController.navigate(
                            Screen.BiomeDetail.passArguments(id)
                        ) {
                            launchSingleTop = true
                        }
                    }
                },
                paddingValues = innerPadding,
                animatedVisibilityScope = this@composable
            )
        }

        composable(Screen.Boss.route) {
            BossScreen(
                modifier = Modifier.padding(10.dp),
                onItemClick = { mainBossId ->
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
                        lastClickTime.longValue = currentTime
                        valheimVikiNavController.navigate(
                            Screen.MainBossDetail.passArguments(mainBossId)
                        ) {
                            launchSingleTop = true
                        }
                    }
                },
                paddingValues = innerPadding,
                animatedVisibilityScope = this@composable
            )
        }
        composable(Screen.MiniBoss.route) {
            MiniBossScreen(
                modifier = Modifier.padding(10.dp),
                onItemClick = { miniBossId ->
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
                        lastClickTime.longValue = currentTime
                        valheimVikiNavController.navigate(
                            Screen.MiniBossDetail.passArguments(miniBossId)
                        ) {
                            launchSingleTop = true
                        }
                    }
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
                onItemClick = { weaponId ,_->
                    valheimVikiNavController.navigate(
                        Screen.WeaponDetail.passWeaponId(
                            weaponId
                        )
                    )
                },
                paddingValues = innerPadding,
            )
        }

        composable(Screen.ArmorList.route) {
            ArmorListScreen(
                modifier = Modifier.padding(10.dp),
                onItemClick = { armorId ,_->
                    valheimVikiNavController.navigate(
                        Screen.ArmorDetail.passArmorId(
                            armorId
                        )
                    )
                },
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
                onItemClick = { itemId ->
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
                onItemClick = { itemId ->
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
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = 50
                    )
                ) + slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight / 4 },
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 0,
                        easing = EaseOutCubic
                    )
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 50))
            },
            arguments = listOf(
                navArgument(BIOME_ARGUMENT_KEY) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val animatedContentScope = this
            BiomeDetailScreen(
                onBack = {
                    valheimVikiNavController.popBackStack()
                },
                animatedVisibilityScope = animatedContentScope
            )
        }
        composable(
            route = Screen.MainBossDetail.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = 50
                    )
                ) + slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight / 4 },
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 0,
                        easing = EaseOutCubic
                    )
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 50))
            },
            arguments = listOf(
                navArgument(MAIN_BOSS_ARGUMENT_KEY) { type = NavType.StringType },
            )
        ) { backStackEntry ->

            MainBossDetailScreen(
                onBack = {
                    valheimVikiNavController.popBackStack()
                },
                animatedVisibilityScope = this@composable,
            )
        }
        composable(
            route = Screen.MiniBossDetail.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = 50
                    )
                ) + slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight / 4 },
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = 0,
                        easing = EaseOutCubic
                    )
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 50))
            },
            arguments = listOf(
                navArgument(MINI_BOSS_ARGUMENT_KEY) { type = NavType.StringType },
            )
        ) { backStackEntry ->

            MiniBossDetailScreen(
                onBack = {
                    valheimVikiNavController.popBackStack()
                },
                animatedVisibilityScope = this@composable,
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

        composable(
            route =Screen.WeaponDetail.route,
            arguments = listOf(
                    navArgument(WEAPON_KEY) { type = NavType.StringType },
        )
        ) {
            WeaponDetailScreen(
                onBack = {
                    valheimVikiNavController.popBackStack()
                },
            )
        }

        composable(
            route =Screen.ArmorDetail.route,
            arguments = listOf(
                navArgument(ARMOR_KEY) { type = NavType.StringType },
            )
        ) {
            WeaponDetailScreen(
                onBack = {
                    valheimVikiNavController.popBackStack()
                },
            )
        }
    }
}


@Composable
fun rememberDrawerItems(): List<DrawerItem> {

    val biomesLabel = stringResource(R.string.biomes)
    val biomesDesc = stringResource(R.string.biomes_section)

    val bossesLabel = stringResource(R.string.bosses)
    val bossesDesc = stringResource(R.string.boss_section)

    val minibossesLabel = stringResource(R.string.minibosses)
    val minibossesDesc = stringResource(R.string.minibosses_section)

    val creaturesLabel = stringResource(R.string.creatures)
    val creaturesDesc = stringResource(R.string.creatures_section)

    val weaponsLabel = stringResource(R.string.weapons)
    val weaponsDesc = stringResource(R.string.weapons_section)

    val armorsLabel = stringResource(R.string.armors)
    val armorsDesc = stringResource(R.string.armor_section)

    val foodLabel = stringResource(R.string.food)
    val foodDesc = stringResource(R.string.food_section)

    val meadsLabel = stringResource(R.string.meads)
    val meadsDesc = stringResource(R.string.mead_section)

    val toolsLabel = stringResource(R.string.tools)
    val toolsDesc = stringResource(R.string.tools_section)

    val materialsLabel = stringResource(R.string.materials)
    val materialsDesc = stringResource(R.string.materials_section)

    val buildingMatsLabel = stringResource(R.string.building_materials)
    val buildingMatsDesc = stringResource(R.string.building_materials_section)

    val oreLabel = stringResource(R.string.ore_deposits)
    val oreDesc = stringResource(R.string.ore_deposits_section)

    val treesLabel = stringResource(R.string.trees)
    val treesDesc = stringResource(R.string.trees_section)

    val poiLabel = stringResource(R.string.points_of_interest)
    val poiDesc = stringResource(R.string.points_of_interest_section)

    // Painters & icons
    val skullPainter = painterResource(R.drawable.skull)
    val ogrePainter = painterResource(R.drawable.ogre)

    val mountainSnowIcon = Lucide.MountainSnow
    val rabbitIcon = Lucide.Rabbit
    val swordsIcon = Lucide.Swords
    val shieldIcon = Lucide.Shield
    val utensilsIcon = Lucide.Utensils
    val flaskIcon = Lucide.FlaskRound
    val gavelIcon = Lucide.Gavel
    val cuboidIcon = Lucide.Cuboid
    val houseIcon = Lucide.House
    val pickaxeIcon = Lucide.Pickaxe
    val treesIcon = Lucide.Trees
    val mapPinnedIcon = Lucide.MapPinned

    val configuration = LocalConfiguration.current
    return remember(configuration) {
        listOf(
            // Biomes
            DrawerItem(
                icon = mountainSnowIcon,
                label = biomesLabel,
                contentDescription = biomesDesc,
                route = Screen.Biome.route
            ),
            // Bosses
            DrawerItem(
                iconPainter = skullPainter,
                label = bossesLabel,
                contentDescription = bossesDesc,
                route = Screen.Boss.route
            ),
            // Mini-bosses
            DrawerItem(
                iconPainter = ogrePainter,
                label = minibossesLabel,
                contentDescription = minibossesDesc,
                route = Screen.MiniBoss.route
            ),
            // Creatures
            DrawerItem(
                icon = rabbitIcon,
                label = creaturesLabel,
                contentDescription = creaturesDesc,
                route = Screen.MobList.route
            ),
            // Weapons
            DrawerItem(
                icon = swordsIcon,
                label = weaponsLabel,
                contentDescription = weaponsDesc,
                route = Screen.WeaponList.route
            ),
            // Armors
            DrawerItem(
                icon = shieldIcon,
                label = armorsLabel,
                contentDescription = armorsDesc,
                route = Screen.ArmorList.route
            ),
            // Food
            DrawerItem(
                icon = utensilsIcon,
                label = foodLabel,
                contentDescription = foodDesc,
                route = Screen.FoodList.route
            ),
            // Meads
            DrawerItem(
                icon = flaskIcon,
                label = meadsLabel,
                contentDescription = meadsDesc,
                route = Screen.MeadList.route
            ),
            // Tools
            DrawerItem(
                icon = gavelIcon,
                label = toolsLabel,
                contentDescription = toolsDesc,
                route = Screen.ToolList.route
            ),
            // Materials
            DrawerItem(
                icon = cuboidIcon,
                label = materialsLabel,
                contentDescription = materialsDesc,
                route = Screen.MaterialCategory.route
            ),
            // Building Materials
            DrawerItem(
                icon = houseIcon,
                label = buildingMatsLabel,
                contentDescription = buildingMatsDesc,
                route = Screen.BuildingMaterialCategory.route
            ),
            // Ore Deposits
            DrawerItem(
                icon = pickaxeIcon,
                label = oreLabel,
                contentDescription = oreDesc,
                route = Screen.OreDeposit.route
            ),
            // Trees
            DrawerItem(
                icon = treesIcon,
                label = treesLabel,
                contentDescription = treesDesc,
                route = Screen.Tree.route
            ),
            // Points of Interest
            DrawerItem(
                icon = mapPinnedIcon,
                label = poiLabel,
                contentDescription = poiDesc,
                route = Screen.PointOfInterest.route
            )
        )
    }
}


val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
