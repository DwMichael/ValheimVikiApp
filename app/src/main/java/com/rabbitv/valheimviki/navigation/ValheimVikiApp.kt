@file:OptIn(
	ExperimentalSharedTransitionApi::class
)

package com.rabbitv.valheimviki.navigation

import android.os.Build
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.food.FoodSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.presentation.armor.ArmorListScreen
import com.rabbitv.valheimviki.presentation.biome.BiomeScreen
import com.rabbitv.valheimviki.presentation.building_material.BuildingMaterialCategoryScreen
import com.rabbitv.valheimviki.presentation.building_material.BuildingMaterialListScreen
import com.rabbitv.valheimviki.presentation.building_material.viewmodel.BuildingMaterialListViewModel
import com.rabbitv.valheimviki.presentation.components.DrawerItem
import com.rabbitv.valheimviki.presentation.components.NavigationDrawer
import com.rabbitv.valheimviki.presentation.crafting.CraftingListScreen
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossScreen
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossScreen
import com.rabbitv.valheimviki.presentation.creatures.mob_list.MobListScreen
import com.rabbitv.valheimviki.presentation.detail.armor.ArmorDetailScreen
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeDetailScreen
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
import com.rabbitv.valheimviki.presentation.detail.mead.MeadDetailScreen
import com.rabbitv.valheimviki.presentation.detail.ore_deposit.OreDepositDetailScreen
import com.rabbitv.valheimviki.presentation.detail.point_of_interest.PointOfInterestDetailScreen
import com.rabbitv.valheimviki.presentation.detail.tool.ToolDetailScreen
import com.rabbitv.valheimviki.presentation.detail.tree.TreeDetailScreen
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
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.S)
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


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainContainer(
	valheimVikiNavController: NavHostController,
	modifier: Modifier = Modifier
) {
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	val scope = rememberCoroutineScope()

	val drawerItems: List<DrawerItem> = rememberDrawerItems()
	val currentBackStackEntry by valheimVikiNavController.currentBackStackEntryAsState()
	val currentDestination = currentBackStackEntry?.destination


	val selectedItem by remember(currentDestination) {
		derivedStateOf {
			currentDestination?.route?.let { route ->
				findSelectedDrawerItem(route, drawerItems)
			} ?: drawerItems.first()
		}
	}

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
		onItemSelected = { item ->

		},
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
				animationSpec = tween(durationMillis = 450, easing = LinearOutSlowInEasing),
				label = "topPaddingAnimation"
			)

			Box(
				modifier = Modifier
					.padding(top = animatedTopPadding)
					.fillMaxSize()
			) {
				ValheimNavGraph(
					valheimVikiNavController = valheimVikiNavController,
					innerPadding = PaddingValues(0.dp)
				)
			}
		}
	}
}


@RequiresApi(Build.VERSION_CODES.S)
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
		startDestination = Screen.Splash,
	) {
		composable<Screen.Splash> {
			SplashScreen(valheimVikiNavController)
		}
		composable<Screen.Welcome> {
			WelcomeScreen(valheimVikiNavController)
		}

		composable<Screen.BiomeList> {
			BiomeScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { id ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						valheimVikiNavController.navigate(
							Screen.BiomeDetail(biomeId = id)
						) {
							launchSingleTop = true
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}

		composable<Screen.BossList> {
			BossScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { mainBossId ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						valheimVikiNavController.navigate(
							Screen.MainBossDetail(mainBossId = mainBossId)
						) {
							launchSingleTop = true
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}
		composable<Screen.MiniBossList> {
			MiniBossScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { miniBossId ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						valheimVikiNavController.navigate(
							Screen.MiniBossDetail(miniBossId = miniBossId)
						) {
							launchSingleTop = true
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}
		composable<Screen.MobList> {
			MobListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { creatureId, creatureSubCategory: CreatureSubCategory ->
					when (creatureSubCategory) {
						CreatureSubCategory.PASSIVE_CREATURE -> valheimVikiNavController.navigate(
							Screen.PassiveCreatureDetail(passiveCreatureId = creatureId)
						)

						CreatureSubCategory.AGGRESSIVE_CREATURE -> valheimVikiNavController.navigate(
							Screen.AggressiveCreatureDetail(aggressiveCreatureId = creatureId)
						)

						CreatureSubCategory.NPC -> valheimVikiNavController.navigate(
							Screen.NpcDetail(npcId = creatureId)
						)

						CreatureSubCategory.BOSS -> null
						CreatureSubCategory.MINI_BOSS -> null
					}
				},
				paddingValues = innerPadding,
			)
		}

		composable<Screen.WeaponList> {
			WeaponListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { weaponId, _ ->
					valheimVikiNavController.navigate(
						Screen.WeaponDetail(weaponId = weaponId)
					)
				},
				paddingValues = innerPadding,
			)
		}

		composable<Screen.ArmorList> {
			ArmorListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { armorId, _ ->
					valheimVikiNavController.navigate(
						Screen.ArmorDetail(armorId = armorId)
					)
				},
				paddingValues = innerPadding,
			)
		}
		composable<Screen.FoodList> {
			FoodListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { foodId, foodSubCategory: FoodSubCategory ->
					valheimVikiNavController.navigate(
						Screen.FoodDetail(
							foodId = foodId,
							foodCategory = foodSubCategory
						)
					)
				},
				paddingValues = innerPadding,
			)
		}

		composable<Screen.MeadList> {
			MeadListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { meadId, meadSubCategory: MeadSubCategory ->
					valheimVikiNavController.navigate(
						Screen.MeadDetail(
							meadId = meadId,
							meadCategory = meadSubCategory
						)
					)
				},
				paddingValues = innerPadding,
			)
		}

		composable<Screen.CraftingObjectsList> {
			CraftingListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { craftingObjectId, _ ->
					valheimVikiNavController.navigate(
						Screen.CraftingObjectDetail(craftingObjectId = craftingObjectId)
					)
				},
				paddingValues = innerPadding,
			)
		}

		composable<Screen.ToolList> {
			ToolListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { toolId, _ ->
					valheimVikiNavController.navigate(
						Screen.ToolDetail(toolId = toolId)
					)

				},
				paddingValues = innerPadding,
			)
		}

		navigation<Screen.MaterialGraph>(
			startDestination = Screen.MaterialCategory
		) {
			composable<Screen.MaterialCategory> { backStackEntry ->
				val parentEntry = remember(backStackEntry) {
					valheimVikiNavController.getBackStackEntry<Screen.MaterialGraph>()
				}
				val vm = hiltViewModel<MaterialListViewModel>(parentEntry)
				MaterialCategoryScreen(
					modifier = Modifier.padding(10.dp),
					paddingValues = innerPadding,
					onGridCategoryClick = {
						valheimVikiNavController.navigate(Screen.MaterialList)
					},
					viewModel = vm
				)
			}

			composable<Screen.MaterialList> { backStackEntry ->
				val parentEntry = remember(backStackEntry) {
					valheimVikiNavController.getBackStackEntry<Screen.MaterialGraph>()
				}
				val vm = hiltViewModel<MaterialListViewModel>(parentEntry)
				MaterialListScreen(
					onItemClick = { itemId, itemCategory: MaterialSubCategory ->

						when (itemCategory) {
							MaterialSubCategory.BOSS_DROP -> valheimVikiNavController.navigate(
								Screen.BossDropDetail(bossDropId = itemId)
							)

							MaterialSubCategory.MINI_BOSS_DROP -> valheimVikiNavController.navigate(
								Screen.MiniBossDropDetail(miniBossDropId = itemId)
							)

							MaterialSubCategory.CREATURE_DROP -> valheimVikiNavController.navigate(
								Screen.MobDropDetail(mobDropId = itemId)
							)

							MaterialSubCategory.FORSAKEN_ALTAR_OFFERING -> valheimVikiNavController.navigate(
								Screen.OfferingsDetail(offeringsMaterialId = itemId)
							)

							MaterialSubCategory.CRAFTED -> valheimVikiNavController.navigate(
								Screen.CraftedMaterialDetail(craftedMaterialId = itemId)
							)

							MaterialSubCategory.METAL -> valheimVikiNavController.navigate(
								Screen.MetalMaterialDetail(metalMaterialId = itemId)
							)

							MaterialSubCategory.MISCELLANEOUS -> valheimVikiNavController.navigate(
								Screen.GeneralMaterialDetail(generalMaterialId = itemId)
							)

							MaterialSubCategory.GEMSTONE -> valheimVikiNavController.navigate(
								Screen.GemstoneDetail(gemstoneId = itemId)
							)

							MaterialSubCategory.SEED -> valheimVikiNavController.navigate(
								Screen.SeedDetail(seedMaterialId = itemId)
							)

							MaterialSubCategory.SHOP -> valheimVikiNavController.navigate(
								Screen.ShopMaterialDetail(shopMaterialId = itemId)
							)

							MaterialSubCategory.VALUABLE ->  valheimVikiNavController.navigate(
								Screen.ValuableDetail(valuableMaterialId = itemId)
							)
						}
					},
					onBackClick = {
						valheimVikiNavController.popBackStack()
					},
					viewModel = vm
				)
			}
		}

		navigation<Screen.BuildingMaterialsGraph>(
			startDestination = Screen.BuildingMaterialCategory
		) {
			composable<Screen.BuildingMaterialCategory> { backStackEntry ->
				val parentEntry = remember(backStackEntry) {
					valheimVikiNavController.getBackStackEntry<Screen.BuildingMaterialsGraph>()
				}
				val vm = hiltViewModel<BuildingMaterialListViewModel>(parentEntry)
				BuildingMaterialCategoryScreen(
					modifier = Modifier.padding(10.dp),
					paddingValues = innerPadding,
					onGridCategoryClick = {
						valheimVikiNavController.navigate(Screen.BuildingMaterialList)
					},
					viewModel = vm
				)
			}

			composable<Screen.BuildingMaterialList> { backStackEntry ->

				val parentEntry = remember(backStackEntry) {
					valheimVikiNavController.getBackStackEntry<Screen.BuildingMaterialsGraph>()
				}
				val vm = hiltViewModel<BuildingMaterialListViewModel>(parentEntry)
				BuildingMaterialListScreen(
					onItemClick = { _, _ -> {} },
					onBackClick = {
						valheimVikiNavController.popBackStack()
					},
					viewModel = vm
				)
			}
		}

		composable<Screen.OreDepositList> {
			OreDepositScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { oreDepositId ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						valheimVikiNavController.navigate(
							Screen.OreDepositDetail(oreDepositId = oreDepositId)
						) {
							launchSingleTop = true
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}

		composable<Screen.TreeGrid> {
			TreeScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { treeId ->
					val currentTime = System.currentTimeMillis()
					if (currentTime - lastClickTime.longValue > clickDebounceMillis) {
						lastClickTime.longValue = currentTime
						valheimVikiNavController.navigate(
							Screen.TreeDetail(treeId = treeId)
						) {
							launchSingleTop = true
						}
					}
				},
				paddingValues = innerPadding,
				animatedVisibilityScope = this@composable
			)
		}

		composable<Screen.PointOfInterestList> {
			PoiListScreen(
				modifier = Modifier.padding(10.dp),
				onItemClick = { pointOfInterestId, _ ->
					valheimVikiNavController.navigate(
						Screen.PointOfInterestDetail(pointOfInterestId = pointOfInterestId)
					)

				},
				paddingValues = innerPadding,
			)
		}


		//Detail Screens
		composable<Screen.BiomeDetail>(
			enterTransition = { enterTransition() },
			popExitTransition = { popExitTransition() }
		) {
			val animatedContentScope = this
			BiomeDetailScreen(
				onBack = { valheimVikiNavController.popBackStack() },
				animatedVisibilityScope = animatedContentScope
			)
		}
		composable<Screen.MainBossDetail>(
			enterTransition = { enterTransition() },
			popExitTransition = { popExitTransition() }
		) {
			MainBossDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				animatedVisibilityScope = this@composable,
			)
		}
		composable<Screen.MiniBossDetail>(
			enterTransition = { enterTransition() },
			popExitTransition = { popExitTransition() }

		) {
			MiniBossDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				animatedVisibilityScope = this@composable,
			)
		}
		composable<Screen.AggressiveCreatureDetail> {
			AggressiveCreatureDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}

		composable<Screen.PassiveCreatureDetail> {
			PassiveCreatureDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.NpcDetail> {
			NpcDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}

		composable<Screen.WeaponDetail> {
			WeaponDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}

		composable<Screen.ArmorDetail> {
			ArmorDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}

		composable<Screen.FoodDetail> { backStackEntry ->
			val args = backStackEntry.toRoute<Screen.FoodDetail>()
			FoodDetailScreen(
				category = args.foodCategory,
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}

		composable<Screen.MeadDetail> { backStackEntry ->
			val args = backStackEntry.toRoute<Screen.MeadDetail>()
			MeadDetailScreen(
				category = args.meadCategory,
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.CraftingObjectDetail> {
			CraftingDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.ToolDetail> {
			ToolDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.OreDepositDetail> {
			OreDepositDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				animatedVisibilityScope = this@composable,
			)
		}
		composable<Screen.TreeDetail> {
			TreeDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
				animatedVisibilityScope = this@composable,
			)
		}

		composable<Screen.PointOfInterestDetail> {
			PointOfInterestDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}

		composable<Screen.BossDropDetail> {
			BossDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}

		composable<Screen.CraftedMaterialDetail> {
			CraftedMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.GeneralMaterialDetail> {
			GeneralMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.MetalMaterialDetail> {
			MetalMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.MiniBossDropDetail> {
			MiniBossDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.MobDropDetail> {
			MobDropDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.OfferingsDetail> {
			OfferingsDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.GemstoneDetail> {
			GemstoneDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}

		composable<Screen.SeedDetail> {
			SeedMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.ShopMaterialDetail> {
			ShopMaterialDetailScreen(
				onBack = {
					valheimVikiNavController.popBackStack()
				},
			)
		}
		composable<Screen.ValuableDetail> {
			ValuableMaterialDetailScreen(
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
	val skullPainter = painterResource(R.drawable.skull)
	val ogrePainter = painterResource(R.drawable.ogre)

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
		listOf(
			// Biomes
			DrawerItem(
				icon = mountainSnowIcon,
				label = biomesLabel,
				contentDescription = biomesDesc,
				screen = Screen.BiomeList
			),
			// Bosses
			DrawerItem(
				iconPainter = skullPainter,
				label = bossesLabel,
				contentDescription = bossesDesc,
				screen = Screen.BossList
			),
			// Mini-bosses
			DrawerItem(
				iconPainter = ogrePainter,
				label = minibossesLabel,
				contentDescription = minibossesDesc,
				screen = Screen.MiniBossList
			),
			// Creatures
			DrawerItem(
				icon = rabbitIcon,
				label = creaturesLabel,
				contentDescription = creaturesDesc,
				screen = Screen.MobList
			),
			// Weapons
			DrawerItem(
				icon = swordsIcon,
				label = weaponsLabel,
				contentDescription = weaponsDesc,
				screen = Screen.WeaponList
			),
			// Armors
			DrawerItem(
				icon = shieldIcon,
				label = armorsLabel,
				contentDescription = armorsDesc,
				screen = Screen.ArmorList
			),
			// Food
			DrawerItem(
				icon = utensilsIcon,
				label = foodLabel,
				contentDescription = foodDesc,
				screen = Screen.FoodList
			),
			// Meads
			DrawerItem(
				icon = flaskIcon,
				label = meadsLabel,
				contentDescription = meadsDesc,
				screen = Screen.MeadList
			),
			// CraftingObjects
			DrawerItem(
				icon = anvilIcon,
				label = craftingObjectsLabel,
				contentDescription = craftingObjectsDesc,
				screen = Screen.CraftingObjectsList
			),
			// Tools
			DrawerItem(
				icon = gavelIcon,
				label = toolsLabel,
				contentDescription = toolsDesc,
				screen = Screen.ToolList
			),
			// Materials
			DrawerItem(
				icon = cuboidIcon,
				label = materialsLabel,
				contentDescription = materialsDesc,
				screen = Screen.MaterialCategory
			),
			// Building Materials
			DrawerItem(
				icon = houseIcon,
				label = buildingMatsLabel,
				contentDescription = buildingMatsDesc,
				screen = Screen.BuildingMaterialCategory
			),
			// Ore Deposits
			DrawerItem(
				icon = pickaxeIcon,
				label = oreLabel,
				contentDescription = oreDesc,
				screen = Screen.OreDepositList
			),
			// Trees
			DrawerItem(
				icon = treesIcon,
				label = treesLabel,
				contentDescription = treesDesc,
				screen = Screen.TreeGrid
			),
			// Points of Interest
			DrawerItem(
				icon = mapPinnedIcon,
				label = poiLabel,
				contentDescription = poiDesc,
				screen = Screen.PointOfInterestList
			)
		)
	}
}

private fun findSelectedDrawerItem(
	currentRoute: String,
	drawerItems: List<DrawerItem>
): DrawerItem {
	val allMatches = drawerItems.filter { item ->
		val screenName = item.screen::class.simpleName ?: ""
		screenName.isNotEmpty() && currentRoute.contains(screenName, ignoreCase = true)
	}

	return allMatches.maxByOrNull { item ->
		(item.screen::class.simpleName ?: "").length
	} ?: drawerItems.first()
}

fun NavDestination.shouldShowTopBar(): Boolean {
	val route = this.route ?: return false
	val mainScreens = listOf(
		"BiomeList", "BossList", "MiniBossList", "MobList",
		"WeaponList", "ArmorList", "FoodList", "MeadList",
		"CraftingObjectsList", "ToolList", "MaterialCategory",
		"BuildingMaterialCategory", "OreDepositList", "TreeGrid", "PointOfInterestList"
	)
	return mainScreens.any { screenName ->
		route.contains(screenName, ignoreCase = true)
	}
}

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
