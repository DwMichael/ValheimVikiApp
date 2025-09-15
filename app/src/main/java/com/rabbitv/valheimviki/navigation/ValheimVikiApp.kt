@file:OptIn(
	ExperimentalSharedTransitionApi::class
)

package com.rabbitv.valheimviki.navigation

import android.content.Intent
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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
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
import com.rabbitv.valheimviki.presentation.components.NavigationDrawer
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
import com.rabbitv.valheimviki.presentation.splash.SplashScreen
import com.rabbitv.valheimviki.presentation.tool.ToolListScreen
import com.rabbitv.valheimviki.presentation.tree.TreeScreen
import com.rabbitv.valheimviki.presentation.weapons.WeaponListScreen
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.launch


@Preview
@Composable
fun ValheimVikiApp() {
	ValheimVikiAppTheme {
		MainContainer()
	}
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainContainer(
	modifier: Modifier = Modifier,
	valheimVikiNavController: NavHostController = rememberNavController(),
) {
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	val scope = rememberCoroutineScope()
	val context = LocalContext.current

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

	NavigationDrawer(
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
						onFeedbackClick = {
							val feedbackFormUrl = "https://forms.gle/vAYCSzkxcjzU6xkr9"
							val intent = Intent(Intent.ACTION_VIEW, feedbackFormUrl.toUri())
							context.startActivity(intent)
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
							innerPadding = PaddingValues(0.dp)
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


@Composable
fun ValheimNavGraph(
	valheimVikiNavController: NavHostController,
	innerPadding: PaddingValues,
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
					valheimVikiNavController.navigate(destination)
				},
			)
		}

		composable<TopLevelDestination.Search> {
			SearchScreen(
				onBack = { valheimVikiNavController.popBackStack() },
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
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
						valheimVikiNavController.navigate(destination) {
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
						valheimVikiNavController.navigate(destination) {
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
						valheimVikiNavController.navigate(destination) {
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
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.ItemDestinations.WeaponList> {
			WeaponListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.ItemDestinations.ArmorList> {
			ArmorListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
				paddingValues = innerPadding,
			)
		}
		composable<ListDestination.FoodDestinations.FoodList> {
			FoodListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.FoodDestinations.MeadList> {
			MeadListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.CraftingDestinations.CraftingObjectsList> {
			CraftingListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
				paddingValues = innerPadding,
			)
		}

		composable<ListDestination.ItemDestinations.ToolList> {
			ToolListScreen(
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
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
						valheimVikiNavController.navigate(destination)
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
						valheimVikiNavController.navigate(destination)
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
						valheimVikiNavController.navigate(destination) {
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
						valheimVikiNavController.navigate(destination) {
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
				modifier = Modifier.padding(10.dp),
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
				},
			)
		}

		composable<CreatureDetailDestination.PassiveCreatureDetail> {
			PassiveCreatureDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<CreatureDetailDestination.NpcDetail> {
			NpcDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
				},
			)
		}

		composable<EquipmentDetailDestination.WeaponDetail> {
			WeaponDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}

		composable<EquipmentDetailDestination.ArmorDetail> {
			ArmorDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}

		composable<EquipmentDetailDestination.ToolDetail> {
			ToolDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
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
					valheimVikiNavController.navigate(destination)
				},
			)
		}

		composable<MaterialDetailDestination.BossDropDetail> {
			BossDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}

		composable<MaterialDetailDestination.CraftedMaterialDetail> {
			CraftedMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.GeneralMaterialDetail> {
			GeneralMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.MetalMaterialDetail> {
			MetalMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.MiniBossDropDetail> {
			MiniBossDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.MobDropDetail> {
			MobDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.OfferingsDetail> {
			OfferingsDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.GemstoneDetail> {
			GemstoneDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}

		composable<MaterialDetailDestination.SeedDetail> {
			SeedMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.ShopMaterialDetail> {
			ShopMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.ValuableDetail> {
			ValuableMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<MaterialDetailDestination.WoodDetail> {
			WoodMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
				},
			)
		}
		composable<BuildingDetailDestination.BuildingMaterialDetail> {
			BuildingMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				onItemClick = { destination ->
					valheimVikiNavController.navigate(destination)
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
					drawerId = 0,
					icon = mountainSnowIcon,
					label = biomesLabel,
					contentDescription = biomesDesc,
					navigationDestination = GridDestination.WorldDestinations.BiomeGrid
				),
				// Bosses
				DrawerItem(
					drawerId = 1,
					iconPainter = skullPainter,
					label = bossesLabel,
					contentDescription = bossesDesc,
					navigationDestination = GridDestination.CreatureDestinations.BossGrid
				),
				// Mini-bosses
				DrawerItem(
					drawerId = 2,
					iconPainter = ogrePainter,
					label = minibossesLabel,
					contentDescription = minibossesDesc,
					navigationDestination = GridDestination.CreatureDestinations.MiniBossGrid
				),
				// Creatures
				DrawerItem(
					drawerId = 3,
					icon = rabbitIcon,
					label = creaturesLabel,
					contentDescription = creaturesDesc,
					navigationDestination = ListDestination.CreatureDestinations.MobList
				),
				// Weapons
				DrawerItem(
					drawerId = 4,
					icon = swordsIcon,
					label = weaponsLabel,
					contentDescription = weaponsDesc,
					navigationDestination = ListDestination.ItemDestinations.WeaponList
				),
				// Armors
				DrawerItem(
					drawerId = 5,
					icon = shieldIcon,
					label = armorsLabel,
					contentDescription = armorsDesc,
					navigationDestination = ListDestination.ItemDestinations.ArmorList
				),
				// Food
				DrawerItem(
					drawerId = 6,
					icon = utensilsIcon,
					label = foodLabel,
					contentDescription = foodDesc,
					navigationDestination = ListDestination.FoodDestinations.FoodList
				),
				// Meads
				DrawerItem(
					drawerId = 7,
					icon = flaskIcon,
					label = meadsLabel,
					contentDescription = meadsDesc,
					navigationDestination = ListDestination.FoodDestinations.MeadList
				),
				// CraftingObjects
				DrawerItem(
					drawerId = 8,
					icon = anvilIcon,
					label = craftingObjectsLabel,
					contentDescription = craftingObjectsDesc,
					navigationDestination = ListDestination.CraftingDestinations.CraftingObjectsList
				),
				// Tools
				DrawerItem(
					drawerId = 9,
					icon = gavelIcon,
					label = toolsLabel,
					contentDescription = toolsDesc,
					navigationDestination = ListDestination.ItemDestinations.ToolList
				),
				// Materials
				DrawerItem(
					drawerId = 10,
					icon = cuboidIcon,
					label = materialsLabel,
					contentDescription = materialsDesc,
					navigationDestination = ListDestination.CraftingDestinations.MaterialCategory
				),
				// Building Materials
				DrawerItem(
					drawerId = 11,
					icon = houseIcon,
					label = buildingMatsLabel,
					contentDescription = buildingMatsDesc,
					navigationDestination = ListDestination.CraftingDestinations.BuildingMaterialCategory
				),
				// Ore Deposits
				DrawerItem(
					drawerId = 12,
					icon = pickaxeIcon,
					label = oreLabel,
					contentDescription = oreDesc,
					navigationDestination = GridDestination.WorldDestinations.OreDepositGrid
				),
				// Trees
				DrawerItem(
					drawerId = 13,
					icon = treesIcon,
					label = treesLabel,
					contentDescription = treesDesc,
					navigationDestination = GridDestination.WorldDestinations.TreeGrid
				),
				// Points of Interest
				DrawerItem(
					drawerId = 14,
					icon = mapPinnedIcon,
					label = poiLabel,
					contentDescription = poiDesc,
					navigationDestination = ListDestination.WorldDestinations.PointOfInterestList
				)
			)
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
