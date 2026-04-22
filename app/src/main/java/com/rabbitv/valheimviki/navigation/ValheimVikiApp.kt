@file:OptIn(
	ExperimentalSharedTransitionApi::class
)

package com.rabbitv.valheimviki.navigation

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.rabbitv.valheimviki.domain.ads.AdManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.composables.icons.lucide.Anvil
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.FlaskRound
import com.composables.icons.lucide.Gavel
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinned
import com.composables.icons.lucide.MountainSnow
import com.composables.icons.lucide.Omega
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.Rabbit
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.Trees
import com.composables.icons.lucide.Utensils
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchConfig.SEARCHABLE_CATEGORIES
import com.rabbitv.valheimviki.presentation.armor.ArmorListScreen
import com.rabbitv.valheimviki.presentation.biome.BiomeGridScreen
import com.rabbitv.valheimviki.presentation.building_material.BuildingMaterialCategoryScreen
import com.rabbitv.valheimviki.presentation.building_material.BuildingMaterialListScreen
import com.rabbitv.valheimviki.presentation.building_material.viewmodel.BuildingMaterialListViewModel
import com.rabbitv.valheimviki.presentation.components.DrawerItem
import com.rabbitv.valheimviki.presentation.components.DrawerItemCollection
import com.rabbitv.valheimviki.presentation.components.FloatingHomeButton
import com.rabbitv.valheimviki.presentation.components.AdaptiveNavigationWrapper
import com.rabbitv.valheimviki.presentation.components.topbar.MainAppBar
import com.rabbitv.valheimviki.presentation.crafting.CraftingListScreen
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossGridScreen
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossesGridScreen
import com.rabbitv.valheimviki.presentation.creatures.mob_list.MobListScreen
import com.rabbitv.valheimviki.presentation.detail.armor.ArmorDetailScreen
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeDetailScreen
import com.rabbitv.valheimviki.presentation.detail.building_material.BuildingMaterialDetailScreen
import com.rabbitv.valheimviki.presentation.detail.crafting.CraftingDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.AggressiveCreatureDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.MainBossDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen.MiniBossDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.npc.NpcDetailScreen
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.PassiveCreatureDetailScreen
import com.rabbitv.valheimviki.presentation.detail.food.FoodDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.boss_drop.BossDropDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.crafted.CraftedMaterialDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.gemstones.GemstoneDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.general.GeneralMaterialDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.metal.MetalMaterialDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.MiniBossDropDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.mob_drop.MobDropDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.offerings.OfferingsDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.seeds.SeedMaterialDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.shop.ShopMaterialDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.valuable.ValuableMaterialDetailScreen
import com.rabbitv.valheimviki.presentation.detail.material.wood.WoodMaterialDetailScreen
import com.rabbitv.valheimviki.presentation.detail.mead.MeadDetailScreen
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.OreDepositDetailScreen
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.PointOfInterestDetailScreen
import com.rabbitv.valheimviki.presentation.detail.tool.ToolDetailScreen
import com.rabbitv.valheimviki.presentation.detail.tree.TreeDetailScreen
import com.rabbitv.valheimviki.presentation.detail.trinket.TrinketDetailScreen
import com.rabbitv.valheimviki.presentation.detail.weapon.WeaponDetailScreen
import com.rabbitv.valheimviki.presentation.favorite.FavoriteScreen
import com.rabbitv.valheimviki.presentation.food.FoodListScreen
import com.rabbitv.valheimviki.presentation.intro.WelcomeScreen
import com.rabbitv.valheimviki.presentation.material.MaterialCategoryScreen
import com.rabbitv.valheimviki.presentation.material.MaterialListScreen
import com.rabbitv.valheimviki.presentation.material.viewmodel.MaterialListViewModel
import com.rabbitv.valheimviki.presentation.mead.MeadListScreen
import com.rabbitv.valheimviki.presentation.ore_deposit.OreDepositScreen
import com.rabbitv.valheimviki.presentation.points_of_interest.PoiListScreen
import com.rabbitv.valheimviki.presentation.search.SearchScreen
import com.rabbitv.valheimviki.presentation.settings.SettingsScreen
import com.rabbitv.valheimviki.presentation.splash.SplashScreen
import com.rabbitv.valheimviki.presentation.tool.ToolListScreen
import com.rabbitv.valheimviki.presentation.tree.TreeScreen
import com.rabbitv.valheimviki.presentation.trinkets.TrinketListScreen
import com.rabbitv.valheimviki.presentation.weapons.WeaponListScreen
import com.rabbitv.valheimviki.ui.adaptive.ProvideAdaptiveLayout
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun ValheimVikiApp(adManager: com.rabbitv.valheimviki.domain.ads.AdManager? = null) {
	ValheimVikiAppTheme {
		ProvideAdaptiveLayout {
			MainContainer(adManager = adManager)
		}
	}
}


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainContainer(
	modifier: Modifier = Modifier,
	valheimVikiNavController: NavHostController = rememberNavController(),
	adManager: AdManager? = null,
) {
	val activity = LocalActivity.current as Activity
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	val scope = rememberCoroutineScope()

	val drawerCollection: DrawerItemCollection = rememberDrawerItems()

	val currentBackStackEntry by valheimVikiNavController.currentBackStackEntryAsState()

	BackHandler(enabled = drawerState.isOpen) {
		scope.launch {
			drawerState.close()
		}
	}

	val showTopAppBar by remember {
		derivedStateOf {
			currentBackStackEntry?.destination?.shouldShowTopBar() ?: false
		}
	}

	val isTransitionActive = remember { mutableStateOf(false) }

	// Pre-load the first interstitial ad as soon as app starts
	LaunchedEffect(Unit) {
		adManager?.preloadAd()
	}

	// Ad-aware navigation: shows interstitial after every N detail screens
	// then proceeds to the destination automatically
	val adAwareNavigate: (DetailDestination, (androidx.navigation.NavOptionsBuilder.() -> Unit)?) -> Unit = remember(adManager, activity) {
		{ destination, builder ->
			val navigateAction = {
				if (builder != null) {
					valheimVikiNavController.navigate(destination, builder)
				} else {
					valheimVikiNavController.navigate(destination)
				}
			}
			
			if (adManager != null && adManager.onDetailScreenVisited()) {
				adManager.showAd(activity) {
					navigateAction()
				}
			} else {
				navigateAction()
			}
		}
	}

	val isOnboardingFlow by remember {
		derivedStateOf {
			val route = currentBackStackEntry?.destination?.route.orEmpty()
			route.contains("TopLevelDestination.Splash") || route.contains("TopLevelDestination.Welcome")
		}
	}

	if (isOnboardingFlow) {
		Box(modifier = Modifier.fillMaxSize()) {
			ValheimNavGraph(
				valheimVikiNavController = valheimVikiNavController,
				innerPadding = PaddingValues(0.dp),
				adAwareNavigate = adAwareNavigate,
			)
		}
	} else {
		AdaptiveNavigationWrapper(
			modifier = modifier,
			drawerState = drawerState,
			scope = scope,
			childNavController = { valheimVikiNavController },
			items = drawerCollection,
			isDetailScreen = { showTopAppBar },
			isTransitionActive = { isTransitionActive.value },
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
							enabled = { !isTransitionActive.value },
							onSearchBarClick = {
								valheimVikiNavController.navigate(TopLevelDestination.Search)
							},
							onBookMarkClick = {
								valheimVikiNavController.navigate(TopLevelDestination.Favorite)
							},
							settingsClick = {
								valheimVikiNavController.navigate(TopLevelDestination.Settings)
							}
						)
					}
				},
			) { innerPadding ->
				val targetTopPadding = if (showTopAppBar) innerPadding.calculateTopPadding() else 0.dp
				val animatedTopPadding by animateDpAsState(
					targetValue = targetTopPadding,
					animationSpec = tween(durationMillis = 450, easing = LinearOutSlowInEasing),
					label = "topPaddingAnimation"
				)

				Box(
					modifier = Modifier
						.padding(top = animatedTopPadding)
						.fillMaxSize()
				) {
					SharedTransitionLayout {
						CompositionLocalProvider(LocalSharedTransitionScope provides this) {
							LaunchedEffect(this.isTransitionActive) {
								isTransitionActive.value =
									!this@SharedTransitionLayout.isTransitionActive
							}

							ValheimNavGraph(
								valheimVikiNavController = valheimVikiNavController,
								innerPadding = PaddingValues(0.dp),
								adAwareNavigate = adAwareNavigate,
							)
						}
					}

					FloatingHomeButton(
						navController = valheimVikiNavController,
						paddingValues = PaddingValues(bottom = 60.dp)
					)
				}
			}
		}
	}
	com.rabbitv.valheimviki.presentation.components.language_popup.LanguageNotificationDialog()
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ValheimNavGraph(
	valheimVikiNavController: NavHostController,
	innerPadding: PaddingValues,
	adAwareNavigate: (DetailDestination, (androidx.navigation.NavOptionsBuilder.() -> Unit)?) -> Unit = { dest, builder ->
		if (builder != null) valheimVikiNavController.navigate(dest, builder) else valheimVikiNavController.navigate(dest)
	},
) {
	val lastClickTime = remember { mutableLongStateOf(0L) }
	val clickDebounceMillis = 500

	val enterTransition = {
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
	}
	val popExitTransition = {
		fadeOut(animationSpec = tween(durationMillis = 50))
	}

	NavHost(
		navController = valheimVikiNavController,
		startDestination = TopLevelDestination.Splash,
	) {
		composable<TopLevelDestination.Splash> {
			SplashScreen(valheimVikiNavController)
		}
		composable<TopLevelDestination.Welcome> {
			WelcomeScreen(valheimVikiNavController)
		}
		composable<TopLevelDestination.Favorite> {
			FavoriteScreen(
				onBack = { valheimVikiNavController.popBackStack() },
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<TopLevelDestination.Settings> {
			SettingsScreen(
				onBack = { valheimVikiNavController.popBackStack() },
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<TopLevelDestination.Search> {
			SearchScreen(
				onBack = { valheimVikiNavController.popBackStack() },
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<GridDestination.WorldDestinations.BiomeGrid> {
			BiomeGridScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						adAwareNavigate(destination) {
							launchSingleTop = true
							popUpTo(destination) {
								inclusive = true
							}
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}

		composable<GridDestination.CreatureDestinations.BossGrid> {
			BossGridScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						adAwareNavigate(destination) {
							launchSingleTop = true
							popUpTo(destination) {
								inclusive = true
							}
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}
		composable<GridDestination.CreatureDestinations.MiniBossGrid> {
			MiniBossesGridScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						adAwareNavigate(destination) {
							launchSingleTop = true
							popUpTo(destination) {
								inclusive = true
							}
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}
		composable<ListDestination.CreatureDestinations.MobList> {
			MobListScreen(
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.ItemDestinations.WeaponList> {
			WeaponListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.ItemDestinations.ArmorList> {
			ArmorListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.ItemDestinations.TrinketList> {
			TrinketListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.FoodDestinations.FoodList> {
			FoodListScreen(
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.FoodDestinations.MeadList> {
			MeadListScreen(
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.CraftingDestinations.CraftingObjectsList> {
			CraftingListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.ItemDestinations.ToolList> {
			ToolListScreen(
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}

		navigation<NavigationGraph.MaterialGraph>(
			startDestination = ListDestination.CraftingDestinations.MaterialCategory
		) {
			composable<ListDestination.CraftingDestinations.MaterialCategory> { backStackEntry ->
				val parentEntry = remember(backStackEntry) {
					valheimVikiNavController.getBackStackEntry<NavigationGraph.MaterialGraph>()
				}
				val vm = hiltViewModel<MaterialListViewModel>(parentEntry)
				MaterialCategoryScreen(
					modifier = Modifier.padding(10.dp),
					paddingValues = innerPadding,
					onGridCategoryClick = {
						valheimVikiNavController.navigate(ListDestination.CraftingDestinations.MaterialList)
					},
					viewModel = vm
				)
			}

			composable<ListDestination.CraftingDestinations.MaterialList> { backStackEntry ->
				val parentEntry = remember(backStackEntry) {
					valheimVikiNavController.getBackStackEntry<NavigationGraph.MaterialGraph>()
				}
				val vm = hiltViewModel<MaterialListViewModel>(parentEntry)
				MaterialListScreen(
					onItemClick = { destination ->
						adAwareNavigate(destination, null)
					},
					onBackClick = {
						valheimVikiNavController.popBackStack()
					},
					viewModel = vm
				)
			}
		}

		navigation<NavigationGraph.BuildingMaterialsGraph>(
			startDestination = ListDestination.CraftingDestinations.BuildingMaterialCategory
		) {
			composable<ListDestination.CraftingDestinations.BuildingMaterialCategory> { backStackEntry ->
				val parentEntry = remember(backStackEntry) {
					valheimVikiNavController.getBackStackEntry<NavigationGraph.BuildingMaterialsGraph>()
				}
				val vm = hiltViewModel<BuildingMaterialListViewModel>(parentEntry)
				BuildingMaterialCategoryScreen(
					modifier = Modifier.padding(10.dp),
					paddingValues = innerPadding,
					onGridCategoryClick = {
						valheimVikiNavController.navigate(ListDestination.CraftingDestinations.BuildingMaterialList)
					},
					viewModel = vm
				)
			}

			composable<ListDestination.CraftingDestinations.BuildingMaterialList> { backStackEntry ->

				val parentEntry = remember(backStackEntry) {
					valheimVikiNavController.getBackStackEntry<NavigationGraph.BuildingMaterialsGraph>()
				}
				val vm = hiltViewModel<BuildingMaterialListViewModel>(parentEntry)
				BuildingMaterialListScreen(
					onItemClick = { destination ->
						adAwareNavigate(destination, null)
					},
					onBackClick = {
						valheimVikiNavController.popBackStack()
					},
					viewModel = vm
				)
			}
		}

		composable<GridDestination.WorldDestinations.OreDepositGrid> {
			OreDepositScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						adAwareNavigate(destination) {
							launchSingleTop = true
							popUpTo(destination) {
								inclusive = true
							}
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}

		composable<GridDestination.WorldDestinations.TreeGrid> {
			TreeScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						adAwareNavigate(destination) {
							launchSingleTop = true
							popUpTo(destination) {
								inclusive = true
							}
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}

		composable<ListDestination.WorldDestinations.PointOfInterestList> {
			PoiListScreen(
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				paddingValues = innerPadding,
			)
		}


		//Detail Screens
		composable<WorldDetailDestination.BiomeDetail>(
			enterTransition = { enterTransition() },
			popExitTransition = { popExitTransition() }
		) {
			val animatedContentScope = this
			BiomeDetailScreen(
				onBack = { valheimVikiNavController.popBackStack() },
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				animatedVisibilityScope = animatedContentScope
			)
		}
		composable<CreatureDetailDestination.MainBossDetail>(
			enterTransition = { enterTransition() },
			popExitTransition = { popExitTransition() }
		) {
			MainBossDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				animatedVisibilityScope = this@composable,
			)
		}
		composable<CreatureDetailDestination.MiniBossDetail>(
			enterTransition = { enterTransition() },
			popExitTransition = { popExitTransition() }

		) {
			MiniBossDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				animatedVisibilityScope = this@composable,
			)
		}
		composable<CreatureDetailDestination.AggressiveCreatureDetail> {
			AggressiveCreatureDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<CreatureDetailDestination.PassiveCreatureDetail> {
			PassiveCreatureDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<CreatureDetailDestination.NpcDetail> {
			NpcDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<ConsumableDetailDestination.FoodDetail> { backStackEntry ->
			val args = backStackEntry.toRoute<ConsumableDetailDestination.FoodDetail>()
			FoodDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				category = args.category,
			)
		}

		composable<ConsumableDetailDestination.MeadDetail> { backStackEntry ->
			val args = backStackEntry.toRoute<ConsumableDetailDestination.MeadDetail>()
			MeadDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				category = args.category,

				)
		}
		composable<BuildingDetailDestination.CraftingObjectDetail> {
			CraftingDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<EquipmentDetailDestination.WeaponDetail> {
			WeaponDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<EquipmentDetailDestination.ArmorDetail> {
			ArmorDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<EquipmentDetailDestination.TrinketDetail> {
			TrinketDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<EquipmentDetailDestination.ToolDetail> {
			ToolDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<WorldDetailDestination.OreDepositDetail>(
			popExitTransition = { popExitTransition() }
		) {
			OreDepositDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				animatedVisibilityScope = this@composable,
			)
		}
		composable<WorldDetailDestination.TreeDetail>(
			popExitTransition = { popExitTransition() }
		) {
			TreeDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
				animatedVisibilityScope = this@composable,
			)
		}

		composable<WorldDetailDestination.PointOfInterestDetail> {
			PointOfInterestDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<MaterialDetailDestination.BossDropDetail> {
			BossDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<MaterialDetailDestination.CraftedMaterialDetail> {
			CraftedMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.GeneralMaterialDetail> {
			GeneralMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.MetalMaterialDetail> {
			MetalMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.MiniBossDropDetail> {
			MiniBossDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.MobDropDetail> {
			MobDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.OfferingsDetail> {
			OfferingsDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.GemstoneDetail> {
			GemstoneDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}

		composable<MaterialDetailDestination.SeedDetail> {
			SeedMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.ShopMaterialDetail> {
			ShopMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.ValuableDetail> {
			ValuableMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<MaterialDetailDestination.WoodDetail> {
			WoodMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
		composable<BuildingDetailDestination.BuildingMaterialDetail> {
			BuildingMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					adAwareNavigate(destination, null)
				},
			)
		}
	}
}


@Composable
fun rememberDrawerItems(): DrawerItemCollection {

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

	val trinketsLabel = stringResource(R.string.trinkets)
	val trinketsDesc = stringResource(R.string.trinket_section)

	val foodLabel = stringResource(R.string.food)
	val foodDesc = stringResource(R.string.food_section)

	val meadsLabel = stringResource(R.string.meads)
	val meadsDesc = stringResource(R.string.mead_section)

	val craftingObjectsLabel = stringResource(R.string.crafting_stations)
	val craftingObjectsDesc = stringResource(R.string.crafting_stations_section)

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
	val skullPainter = painterResource(R.drawable.boss_1)
	val ogrePainter = painterResource(R.drawable.miniboss)

	val mountainSnowIcon = Lucide.MountainSnow
	val rabbitIcon = Lucide.Rabbit
	val swordsIcon = Lucide.Swords
	val shieldIcon = Lucide.Shield
	val trinketIcon = Lucide.Omega
	val utensilsIcon = Lucide.Utensils
	val flaskIcon = Lucide.FlaskRound
	val anvilIcon = Lucide.Anvil
	val gavelIcon = Lucide.Gavel
	val cuboidIcon = Lucide.Cuboid
	val houseIcon = Lucide.House
	val pickaxeIcon = Lucide.Pickaxe
	val treesIcon = Lucide.Trees
	val mapPinnedIcon = Lucide.MapPinned

	val configuration = LocalConfiguration.current
	return remember(configuration) {
		DrawerItemCollection(
			listOf(
				// Biomes
				DrawerItem(
					icon = mountainSnowIcon,
					label = biomesLabel,
					contentDescription = biomesDesc,
					navigationDestination = GridDestination.WorldDestinations.BiomeGrid
				),
				// Bosses
				DrawerItem(
					iconPainter = skullPainter,
					label = bossesLabel,
					contentDescription = bossesDesc,
					navigationDestination = GridDestination.CreatureDestinations.BossGrid
				),
				// Mini-bosses
				DrawerItem(
					iconPainter = ogrePainter,
					label = minibossesLabel,
					contentDescription = minibossesDesc,
					navigationDestination = GridDestination.CreatureDestinations.MiniBossGrid
				),
				// Creatures
				DrawerItem(
					icon = rabbitIcon,
					label = creaturesLabel,
					contentDescription = creaturesDesc,
					navigationDestination = ListDestination.CreatureDestinations.MobList
				),
				// Weapons
				DrawerItem(
					icon = swordsIcon,
					label = weaponsLabel,
					contentDescription = weaponsDesc,
					navigationDestination = ListDestination.ItemDestinations.WeaponList
				),
				// Armors
				DrawerItem(
					icon = shieldIcon,
					label = armorsLabel,
					contentDescription = armorsDesc,
					navigationDestination = ListDestination.ItemDestinations.ArmorList
				),
				// Trinkets
				DrawerItem(
					icon = trinketIcon,
					label = trinketsLabel,
					contentDescription = trinketsDesc,
					navigationDestination = ListDestination.ItemDestinations.TrinketList
				),

				// Food
				DrawerItem(
					icon = utensilsIcon,
					label = foodLabel,
					contentDescription = foodDesc,
					navigationDestination = ListDestination.FoodDestinations.FoodList
				),
				// Meads
				DrawerItem(
					icon = flaskIcon,
					label = meadsLabel,
					contentDescription = meadsDesc,
					navigationDestination = ListDestination.FoodDestinations.MeadList
				),
				// CraftingObjects
				DrawerItem(
					icon = anvilIcon,
					label = craftingObjectsLabel,
					contentDescription = craftingObjectsDesc,
					navigationDestination = ListDestination.CraftingDestinations.CraftingObjectsList
				),
				// Tools
				DrawerItem(
					icon = gavelIcon,
					label = toolsLabel,
					contentDescription = toolsDesc,
					navigationDestination = ListDestination.ItemDestinations.ToolList
				),
				// Materials
				DrawerItem(
					icon = cuboidIcon,
					label = materialsLabel,
					contentDescription = materialsDesc,
					navigationDestination = ListDestination.CraftingDestinations.MaterialCategory
				),
				// Building Materials
				DrawerItem(
					icon = houseIcon,
					label = buildingMatsLabel,
					contentDescription = buildingMatsDesc,
					navigationDestination = ListDestination.CraftingDestinations.BuildingMaterialCategory
				),
				// Ore Deposits
				DrawerItem(
					icon = pickaxeIcon,
					label = oreLabel,
					contentDescription = oreDesc,
					navigationDestination = GridDestination.WorldDestinations.OreDepositGrid
				),
				// Trees
				DrawerItem(
					icon = treesIcon,
					label = treesLabel,
					contentDescription = treesDesc,
					navigationDestination = GridDestination.WorldDestinations.TreeGrid
				),
				// Points of Interest
				DrawerItem(
					icon = mapPinnedIcon,
					label = poiLabel,
					contentDescription = poiDesc,
					navigationDestination = ListDestination.WorldDestinations.PointOfInterestList
				)
			).mapIndexed { index, item ->
				item.copy(drawerId = index)
			}
		)
	}
}


fun NavDestination.shouldShowTopBar(): Boolean {
	val route = this.route ?: return false
	return SEARCHABLE_CATEGORIES.any { screenName ->
		route.contains(screenName, ignoreCase = true)
	}
}

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
