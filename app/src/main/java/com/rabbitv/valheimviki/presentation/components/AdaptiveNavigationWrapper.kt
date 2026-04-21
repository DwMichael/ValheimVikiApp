package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.adaptive.LocalAdaptiveLayoutInfo
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ForestGreen40Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryText
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Adaptive navigation wrapper following Android canonical layouts:
 *  - Compact  → ModalNavigationDrawer (hamburger menu, swipe gesture)
 *  - Medium   → NavigationRail (icon strip on the left side)
 *  - Expanded → PermanentNavigationDrawer (always-visible full drawer)
 *
 * @see <a href="https://developer.android.com/develop/ui/compose/layouts/adaptive/build-adaptive-navigation">Build adaptive navigation</a>
 * @see <a href="https://developer.android.com/develop/ui/compose/layouts/adaptive/canonical-layouts">Canonical layouts</a>
 */
@Composable
fun AdaptiveNavigationWrapper(
	modifier: Modifier = Modifier,
	drawerState: DrawerState,
	scope: CoroutineScope,
	childNavController: () -> NavHostController,
	items: DrawerItemCollection,
	isDetailScreen: () -> Boolean,
	isTransitionActive: () -> Boolean,
	content: @Composable () -> Unit,
) {
	val adaptiveInfo = LocalAdaptiveLayoutInfo.current
	val selectedId = rememberSaveable { mutableIntStateOf(0) }

	// Track selected item from nav back stack
	DisposableEffect(Unit) {
		val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
			val bestMatch = items.drawerItems
				.filter { item ->
					destination.hierarchy.any { nav ->
						val simple = item.navigationDestination::class.simpleName ?: return@any false
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

	when {
		// ── Expanded (≥840dp): Permanent drawer always visible ──
		adaptiveInfo.isExpandedWidth -> {
			PermanentNavigationDrawer(
				modifier = modifier.fillMaxSize().testTag("PermanentNavigationDrawer"),
				drawerContent = {
					PermanentDrawerSheet(
						modifier = Modifier.width(280.dp),
						drawerContainerColor = ForestGreen40Dark,
					) {
						PermanentDrawerContent(
							items = items.drawerItems,
							selectedItem = { selectedId.intValue },
							onItemClick = onItemClick,
						)
					}
				},
			) {
				content()
			}
		}

		// ── Medium (600–839dp): NavigationRail on the left ──
		adaptiveInfo.isMediumWidth -> {
			Row(modifier = modifier.fillMaxSize()) {
				NavigationRailContent(
					items = items.drawerItems,
					selectedItem = { selectedId.intValue },
					onItemClick = onItemClick,
				)
				content()
			}
		}

		// ── Compact (<600dp): Modal drawer (hamburger, swipe) ──
		else -> {
			ModalNavigationDrawer(
				modifier = modifier.fillMaxSize().testTag("ModalNavigationDrawer"),
				drawerState = drawerState,
				gesturesEnabled = true,
				drawerContent = {
					ModalDrawerSheet(
						modifier = Modifier.fillMaxWidth(0.8f),
						drawerContainerColor = ForestGreen40Dark,
					) {
						ModalDrawerContent(
							items = items.drawerItems,
							selectedItem = { selectedId.intValue },
							onItemClick = onItemClick,
						)
					}
				},
			) {
				content()
			}
		}
	}
}

// ══════════════════════════════════════════════════════════
//  Permanent Drawer Content (Expanded)
// ══════════════════════════════════════════════════════════

@Composable
private fun PermanentDrawerContent(
	items: List<DrawerItem>,
	selectedItem: () -> Int,
	onItemClick: (DrawerItem) -> Unit,
) {
	LazyColumn(
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		item("DrawerHeader") { DrawerHeaderCompact() }
		item("Spacer1") { Spacer(Modifier.height(12.dp)) }
		item("Divider") { HorizontalDivider(color = PrimaryWhite.copy(alpha = 0.2f)) }
		item("Spacer2") { Spacer(Modifier.height(12.dp)) }
		items(items, { it.label + "permanent" }) { item ->
			NavigationDrawerItem(
				colors = NavigationDrawerItemDefaults.colors(
					selectedIconColor = PrimaryText,
					selectedTextColor = PrimaryText,
					selectedContainerColor = ForestGreen10Dark,
					unselectedIconColor = PrimaryWhite,
					unselectedTextColor = PrimaryWhite,
					unselectedContainerColor = Color.Transparent,
				),
				icon = { DrawerItemIcon(item) },
				label = {
					Text(
						item.label,
						style = MaterialTheme.typography.bodyLarge,
						fontWeight = FontWeight.Normal,
					)
				},
				selected = item.drawerId == selectedItem(),
				onClick = { onItemClick(item) },
				modifier = Modifier
					.height(48.dp)
					.padding(NavigationDrawerItemDefaults.ItemPadding),
			)
		}
		item("BottomSpacer") { Spacer(Modifier.height(24.dp)) }
	}
}

// ══════════════════════════════════════════════════════════
//  NavigationRail Content (Medium)
// ══════════════════════════════════════════════════════════

@Composable
private fun NavigationRailContent(
	items: List<DrawerItem>,
	selectedItem: () -> Int,
	onItemClick: (DrawerItem) -> Unit,
) {
	NavigationRail(
		modifier = Modifier.fillMaxHeight().testTag("NavigationRail"),
		containerColor = ForestGreen40Dark,
		header = {
			Image(
				modifier = Modifier
					.padding(top = 12.dp, bottom = 8.dp)
					.size(36.dp),
				painter = painterResource(R.drawable.valheim_viki_oval),
				contentDescription = "Logo",
				contentScale = ContentScale.Crop,
			)
		},
	) {
		Column(
			modifier = Modifier
				.fillMaxHeight()
				.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			items.forEach { item ->
				NavigationRailItem(
					selected = item.drawerId == selectedItem(),
					onClick = { onItemClick(item) },
					icon = { DrawerItemIcon(item, size = 22) },
					label = null, // icons-only for rail
					colors = androidx.compose.material3.NavigationRailItemDefaults.colors(
						selectedIconColor = PrimaryText,
						unselectedIconColor = PrimaryWhite,
						indicatorColor = ForestGreen10Dark,
					),
				)
			}
		}
	}
}

// ══════════════════════════════════════════════════════════
//  Modal Drawer Content (Compact)
// ══════════════════════════════════════════════════════════

@Composable
private fun ModalDrawerContent(
	items: List<DrawerItem>,
	selectedItem: () -> Int,
	onItemClick: (DrawerItem) -> Unit,
) {
	LazyColumn(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		item("DrawerHeader") { DrawerHeaderCompact() }
		item("Spacer1") { Spacer(Modifier.height(12.dp)) }
		item("Divider") { HorizontalDivider() }
		item("Spacer2") { Spacer(Modifier.height(12.dp)) }
		items(items, { it.label + "modal" }) { item ->
			NavigationDrawerItem(
				colors = NavigationDrawerItemDefaults.colors(
					selectedIconColor = PrimaryText,
					selectedTextColor = PrimaryText,
					selectedContainerColor = ForestGreen10Dark,
					unselectedIconColor = PrimaryWhite,
					unselectedTextColor = PrimaryWhite,
					unselectedContainerColor = Color.Transparent,
				),
				icon = { DrawerItemIcon(item) },
				label = {
					Text(
						item.label,
						style = MaterialTheme.typography.bodyLarge,
						fontWeight = FontWeight.Normal,
					)
				},
				selected = item.drawerId == selectedItem(),
				onClick = { onItemClick(item) },
				modifier = Modifier
					.height(48.dp)
					.padding(NavigationDrawerItemDefaults.ItemPadding),
			)
		}
		item("BottomSpacer") { Spacer(Modifier.height(24.dp)) }
	}
}

// ══════════════════════════════════════════════════════════
//  Shared Components
// ══════════════════════════════════════════════════════════

@Composable
private fun DrawerHeaderCompact() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 12.dp, top = 12.dp, end = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Start,
	) {
		Image(
			modifier = Modifier.size(42.dp),
			painter = painterResource(R.drawable.valheim_viki_oval),
			contentDescription = "Logo",
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
private fun DrawerItemIcon(item: DrawerItem, size: Int = 24) {
	if (item.iconPainter != null) {
		Icon(
			painter = item.iconPainter,
			contentDescription = item.contentDescription,
			modifier = Modifier.size(size.dp),
		)
	} else {
		item.icon?.let {
			Icon(
				imageVector = it,
				contentDescription = item.contentDescription,
				modifier = Modifier.size(size.dp),
			)
		}
	}
}
