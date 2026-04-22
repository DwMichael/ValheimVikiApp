package com.rabbitv.valheimviki.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
import com.rabbitv.valheimviki.navigation.GridDestination
import com.rabbitv.valheimviki.navigation.ListDestination
import com.rabbitv.valheimviki.navigation.NavigationDestination
import com.rabbitv.valheimviki.ui.adaptive.LocalAdaptiveLayoutInfo
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ForestGreen40Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryText
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Immutable
data class DrawerItem(
	val drawerId: Int = -1,
	val iconPainter: Painter? = null,
	val icon: ImageVector? = null,
	val label: String,
	val contentDescription: String,
	val navigationDestination: NavigationDestination
)

@Immutable
data class DrawerItemCollection(
	val drawerItems: List<DrawerItem>
)

@Composable
fun NavigationDrawer(
	modifier: Modifier,
	drawerState: DrawerState,
	scope: CoroutineScope,
	childNavController: () -> NavHostController,
	items: DrawerItemCollection,
	isDetailScreen: () -> Boolean,
	isTransitionActive: () -> Boolean,
	content: @Composable () -> Unit,
) {


	val selectedId = rememberSaveable { mutableIntStateOf(0) }

	DisposableEffect(Unit) {
		val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
			val bestMatch = items.drawerItems
				.filter { item ->
					destination.hierarchy.any { nav ->
						val simple =
							item.navigationDestination::class.simpleName ?: return@any false
						nav.route?.contains(simple, ignoreCase = true) == true
					}
				}
				.maxByOrNull { item ->
					item.navigationDestination::class.simpleName?.length ?: 0
				}

			bestMatch?.let { selectedId.intValue = it.drawerId }
		}

		childNavController().addOnDestinationChangedListener(listener)
		onDispose { childNavController().removeOnDestinationChangedListener(listener) }
	}

	val onItemClick = remember(scope, childNavController) {
		{ item: DrawerItem ->
			val currentRoute = childNavController().currentDestination?.route
			scope.launch { drawerState.close() }
			if (item.navigationDestination.toString() != currentRoute) {
				childNavController().navigate(item.navigationDestination) {
					popUpTo(childNavController().graph.findStartDestination().id) {
						saveState = true
					}
					launchSingleTop = true
					restoreState = true
				}
			}
		}
	}

	Box(modifier = modifier.fillMaxSize()) {
		ModalNavigationDrawer(
			modifier = Modifier
				.fillMaxSize()
				.testTag("ModalNavigationDrawer"),
			drawerState = drawerState,
			gesturesEnabled = isDetailScreen() && !isTransitionActive(),
			drawerContent = {
				DrawerContent(
					items = items.drawerItems,
					selectedItem = { selectedId.intValue },
					onItemClick = onItemClick,
				)
			},
		) {
			content()
		}
		if (drawerState.isOpen) {
			Box(
				modifier = Modifier
					.fillMaxHeight()
					.fillMaxWidth(0.2f)
					.align(Alignment.CenterEnd)
					.clickable {
						scope.launch { drawerState.close() }
					}
			)
		}
	}
}

@Composable
private fun DrawerContent(
	items: List<DrawerItem>,
	selectedItem: () -> Int,
	onItemClick: (DrawerItem) -> Unit,

	) {
	ModalDrawerSheet(
		modifier = Modifier.fillMaxWidth(
			if (LocalAdaptiveLayoutInfo.current.isExpandedWidth) 0.45f
			else if (LocalAdaptiveLayoutInfo.current.isMediumWidth) 0.6f
			else 0.8f
		),
		drawerContainerColor = ForestGreen40Dark,
	) {
		LazyColumn(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			item("DrawerHeader") { DrawerHeader() }
			item("DrawerHeaderSpacer") { Spacer(Modifier.height(12.dp)) }
			item("DrawerHorizontalDivider") { HorizontalDivider() }
			item("DrawerHorizontalDividerSpacer") { Spacer(Modifier.height(12.dp)) }

			items(items, { item -> item.label + "drawerItem" }) { item ->
				DrawerNavigationItem(
					item = item,
					isSelected = { item.drawerId == selectedItem() },
					onClick = { onItemClick(item) },
				)
			}
			item("DrawerItemSpacer") { Spacer(Modifier.height(24.dp)) }
		}
	}
}

@Composable
private fun DrawerHeader() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 12.dp, top = 12.dp, end = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Start
	) {
		Image(
			modifier = Modifier.size(42.dp),
			painter = painterResource(R.drawable.valheim_viki_oval),
			contentDescription = "DrawerLogoImage",
			contentScale = ContentScale.Crop,
		)
		Spacer(Modifier.padding(12.dp))
		Text(
			text = stringResource(R.string.app_name),
			fontWeight = FontWeight.Medium,
			fontSize = 28.sp,
			color = MaterialTheme.colorScheme.onPrimaryContainer,
		)
	}
}

@Composable
private fun DrawerNavigationItem(
	item: DrawerItem,
	isSelected: () -> Boolean,
	onClick: () -> Unit,
) {

	NavigationDrawerItem(
		colors = NavigationDrawerItemDefaults.colors(
			selectedIconColor = PrimaryText,
			selectedTextColor = PrimaryText,
			selectedContainerColor = ForestGreen10Dark,
			unselectedIconColor = PrimaryWhite,
			unselectedTextColor = PrimaryWhite,
			unselectedContainerColor = Color.Transparent,
		),
		icon = {
			if (item.iconPainter != null) {
				Icon(
					painter = item.iconPainter,
					contentDescription = item.contentDescription,
					modifier = Modifier.size(24.dp)
				)
			} else {
				item.icon?.let {
					Icon(
						imageVector = it,
						contentDescription = item.contentDescription,
						modifier = Modifier.size(24.dp)
					)
				}
			}
		},
		label = {
			Text(
				item.label,
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Normal,
			)
		},
		selected = isSelected(),
		onClick = onClick,
		modifier = Modifier
			.height(48.dp)
			.padding(NavigationDrawerItemDefaults.ItemPadding)
	)
}


@Preview(name = "NavigationDrawerImage", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationDrawerImage() {

	ValheimVikiAppTheme {

		Image(
			modifier = Modifier
				.padding(start = 16.dp, top = 24.dp, end = 12.dp)
				.size(80.dp),
			painter = painterResource(R.drawable.valheim_viki_oval),
			contentDescription = "DrawerBackground",
			contentScale = ContentScale.Crop,

			)

	}
}


@Preview(name = "NavigationDrawer", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNavigationDrawer() {
	val items = listOf(
		DrawerItem(
			icon = Lucide.MountainSnow,
			label = stringResource(R.string.biomes),
			contentDescription = stringResource(R.string.biomes_section),
			navigationDestination = GridDestination.WorldDestinations.BiomeGrid,
		),
		DrawerItem(
			iconPainter = painterResource(R.drawable.boss_1),
			label = stringResource(R.string.bosses),
			contentDescription = stringResource(R.string.boss_section),
			navigationDestination = GridDestination.CreatureDestinations.BossGrid
		),
		DrawerItem(
			iconPainter = painterResource(R.drawable.miniboss),
			label = stringResource(R.string.minibosses),
			contentDescription = stringResource(R.string.minibosses_section),
			navigationDestination = GridDestination.CreatureDestinations.MiniBossGrid
		),
		DrawerItem(
			icon = Lucide.Rabbit,
			label = stringResource(R.string.creatures),
			contentDescription = stringResource(R.string.creatures_section),
			navigationDestination = ListDestination.CreatureDestinations.MobList
		),
		DrawerItem(
			icon = Lucide.Swords,
			label = stringResource(R.string.weapons),
			contentDescription = stringResource(R.string.weapons_section),
			navigationDestination = ListDestination.ItemDestinations.WeaponList
		),
		DrawerItem(
			icon = Lucide.Shield,
			label = stringResource(R.string.armors),
			contentDescription = stringResource(R.string.armor_section),
			navigationDestination = ListDestination.ItemDestinations.ArmorList
		),
		DrawerItem(
			icon = Lucide.Omega,
			label = stringResource(R.string.trinkets),
			contentDescription = stringResource(R.string.trinket_section),
			navigationDestination = ListDestination.ItemDestinations.TrinketList
		),
		DrawerItem(
			icon = Lucide.Utensils,
			label = stringResource(R.string.food),
			contentDescription = stringResource(R.string.food_section),
			navigationDestination = ListDestination.FoodDestinations.FoodList
		),
		DrawerItem(
			icon = Lucide.FlaskRound,
			label = stringResource(R.string.meads),
			contentDescription = stringResource(R.string.mead_section),
			navigationDestination = ListDestination.FoodDestinations.MeadList
		),

		DrawerItem(

			icon = Lucide.Anvil,
			label = stringResource(R.string.crafting_stations),
			contentDescription = stringResource(R.string.crafting_stations_section),
			navigationDestination = ListDestination.CraftingDestinations.CraftingObjectsList
		),
		DrawerItem(
			icon = Lucide.Gavel,
			label = stringResource(R.string.tools),
			contentDescription = stringResource(R.string.tools_section),
			navigationDestination = ListDestination.ItemDestinations.ToolList
		),
		DrawerItem(
			icon = Lucide.Cuboid,
			label = stringResource(R.string.materials),
			contentDescription = stringResource(R.string.materials_section),
			navigationDestination = ListDestination.CraftingDestinations.MaterialCategory
		),
		DrawerItem(
			icon = Lucide.House,
			label = stringResource(R.string.building_materials),
			contentDescription = stringResource(R.string.building_materials_section),
			navigationDestination = ListDestination.CraftingDestinations.BuildingMaterialCategory
		),
		DrawerItem(
			icon = Lucide.Pickaxe,
			label = stringResource(R.string.ore_deposits),
			contentDescription = stringResource(R.string.ore_deposits_section),
			navigationDestination = GridDestination.WorldDestinations.OreDepositGrid
		),
		DrawerItem(
			icon = Lucide.Trees,
			label = stringResource(R.string.trees),
			contentDescription = stringResource(R.string.trees_section),
			navigationDestination = GridDestination.WorldDestinations.TreeGrid
		),
		DrawerItem(
			icon = Lucide.MapPinned,
			label = stringResource(R.string.points_of_interest),
			contentDescription = stringResource(R.string.points_of_interest_section),
			navigationDestination = ListDestination.WorldDestinations.PointOfInterestList
		)
	).mapIndexed { index, item ->
		item.copy(drawerId = index)
	}

	val nav = rememberNavController()
	ValheimVikiAppTheme {
		NavigationDrawer(
			modifier = Modifier,
			drawerState = rememberDrawerState(DrawerValue.Open),
			scope = rememberCoroutineScope(),
			childNavController = { nav },
			items = DrawerItemCollection(items),
			content = {},
			isDetailScreen = { false },
			isTransitionActive = { false },
		)
	}
}