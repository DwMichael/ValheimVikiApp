package com.rabbitv.valheimviki.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.rememberCoroutineScope
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

import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ForestGreen40Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryText
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class DrawerItem(
	val iconPainter: Painter? = null,
	val icon: ImageVector? = null,
	val label: String,
	val contentDescription: String,
	val navigationDestination: NavigationDestination
)

@Composable
fun NavigationDrawer(
	modifier: Modifier,
	drawerState: DrawerState,
	scope: CoroutineScope,
	childNavController: NavHostController,
	items: List<DrawerItem>,
	selectedItem: DrawerItem,
	isDetailScreen: Boolean,
	isTransitionActive: Boolean,
	content: @Composable () -> Unit,
) {
	ModalNavigationDrawer(
		modifier = modifier
			.fillMaxSize()
			.testTag("NavigationDrawer"),
		drawerState = drawerState,
		gesturesEnabled = isDetailScreen && !isTransitionActive,
		drawerContent = {
			DrawerContent(
				items = items,
				selectedItem = selectedItem,
				onItemClick = { item ->
					if (item != selectedItem) {
						childNavController.navigate(item.navigationDestination) {
							popUpTo(childNavController.graph.findStartDestination().id) {
								saveState = true
							}
							launchSingleTop = true
							restoreState = true
						}
					}
					scope.launch { drawerState.close() }
				}
			)
		},
	) {
		content()
	}
}

@Composable
private fun DrawerContent(
	items: List<DrawerItem>,
	selectedItem: DrawerItem,
	onItemClick: (DrawerItem) -> Unit,
) {
	ModalDrawerSheet(
		modifier = Modifier.fillMaxWidth(0.92f),
		drawerContainerColor = ForestGreen40Dark,
	) {
		Column(
			Modifier.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			DrawerHeader()
			Spacer(Modifier.height(12.dp))
			HorizontalDivider()
			Spacer(Modifier.height(12.dp))

			items.forEach { item ->
				DrawerNavigationItem(
					item = item,
					isSelected = item == selectedItem,
					onClick = { onItemClick(item) }
				)
			}
			Spacer(Modifier.height(24.dp))
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
			text = "ValheimViki",
			fontWeight = FontWeight.Medium,
			fontSize = 28.sp,
			color = MaterialTheme.colorScheme.onPrimaryContainer,
		)
	}
}

@Composable
private fun DrawerNavigationItem(
	item: DrawerItem,
	isSelected: Boolean,
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
				fontWeight = FontWeight.Normal,
				lineHeight = 20.sp,
				fontSize = 16.sp,
			)
		},
		selected = isSelected,
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
			label = "Biomes",
			contentDescription = "List of Biomes",
			navigationDestination = GridDestination.WorldDestinations.BiomeGrid
		),
		DrawerItem(
			iconPainter = painterResource(R.drawable.boss_1),
			label = "Bosses",
			contentDescription = "Bosses section",
			navigationDestination = GridDestination.CreatureDestinations.BossGrid
		),
		DrawerItem(
			iconPainter = painterResource(R.drawable.miniboss),
			label = "MiniBosses",
			contentDescription = "MiniBosses section",
			navigationDestination = GridDestination.CreatureDestinations.MiniBossGrid
		),
		DrawerItem(
			icon = Lucide.Rabbit,
			label = "Creatures",
			contentDescription = "Creatures section",
			navigationDestination = ListDestination.CreatureDestinations.MobList
		),
		DrawerItem(
			icon = Lucide.Swords,
			label = "Weapons",
			contentDescription = "Weapons section",
			navigationDestination = ListDestination.ItemDestinations.WeaponList
		),
		DrawerItem(
			icon = Lucide.Shield,
			label = "Armor",
			contentDescription = "Armor section",
			navigationDestination = ListDestination.ItemDestinations.ArmorList
		),
		DrawerItem(
			icon = Lucide.Utensils,
			label = "Food",
			contentDescription = "Food section",
			navigationDestination = ListDestination.FoodDestinations.FoodList
		),
		DrawerItem(
			icon = Lucide.FlaskRound,
			label = "Mead",
			contentDescription = "Mead section",
			navigationDestination = ListDestination.FoodDestinations.MeadList
		),

		DrawerItem(
			icon = Lucide.Anvil,
			label = "Crafting Stations",
			contentDescription = "Crafting Station section",
			navigationDestination = ListDestination.CraftingDestinations.CraftingObjectsList
		),
		DrawerItem(
			icon = Lucide.Gavel,
			label = "Tools",
			contentDescription = "Tools section",
			navigationDestination = ListDestination.ItemDestinations.ToolList
		),
		DrawerItem(
			icon = Lucide.Cuboid,
			label = "Materials",
			contentDescription = "Materials section",
			navigationDestination = ListDestination.CraftingDestinations.MaterialCategory
		),
		DrawerItem(
			icon = Lucide.House,
			label = "Building Materials",
			contentDescription = "Building Materials section",
			navigationDestination = ListDestination.CraftingDestinations.BuildingMaterialCategory
		),
		DrawerItem(
			icon = Lucide.Pickaxe,
			label = "Ore Deposits",
			contentDescription = "Ore Deposits section",
			navigationDestination = GridDestination.WorldDestinations.OreDepositGrid
		),
		DrawerItem(
			icon = Lucide.Trees,
			label = "Trees",
			contentDescription = "Trees section",
			navigationDestination = GridDestination.WorldDestinations.TreeGrid
		),
		DrawerItem(
			icon = Lucide.MapPinned,
			label = "Points Of Interest",
			contentDescription = "Points Of Interest section",
			navigationDestination = ListDestination.WorldDestinations.PointOfInterestList
		)
	)

	ValheimVikiAppTheme {
		NavigationDrawer(
			modifier = Modifier,
			drawerState = rememberDrawerState(DrawerValue.Open),
			scope = rememberCoroutineScope(),
			childNavController = rememberNavController(),
			items = items,
			selectedItem = items[0],
			content = {},
			isDetailScreen = false,
			isTransitionActive = false,
		)
	}
}