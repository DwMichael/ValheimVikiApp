package com.rabbitv.valheimviki.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.navigation.shouldShowTopBar
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite

@Composable
fun FloatingHomeButton(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	paddingValues: PaddingValues = PaddingValues(16.dp)
) {
	val current by navController.currentBackStackEntryAsState()
	val shouldShow by remember(current) {
		derivedStateOf {
			val currDest = current?.destination
			val prevDest = navController.previousBackStackEntry?.destination
			val currIsDetail = currDest?.let { !it.shouldShowTopBar() } ?: false
			val prevIsDetail = prevDest?.let { !it.shouldShowTopBar() } ?: false
			currIsDetail && prevIsDetail
		}
	}
	AnimatedVisibility(
		visible = shouldShow,
		enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn(
			tween(300)
		),
		exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut(
			tween(300)
		),
		modifier = modifier
	) {
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.BottomCenter
		) {
			FloatingActionButton(
				onClick = {
					val startDestinationRoute = navController.graph.findStartDestination().route
					startDestinationRoute?.let { route ->
						navController.navigate(route) {
							popUpTo(navController.graph.findStartDestination().id) {
								saveState = true
							}
							launchSingleTop = true
							restoreState = true
						}
					}
				},
				modifier = Modifier
                    .padding(paddingValues)
                    .size(56.dp),
				containerColor = ForestGreen10Dark,
				contentColor = PrimaryWhite
			) {
				Icon(
					imageVector = Lucide.House,
					contentDescription = "Go to Home",
					modifier = Modifier.size(24.dp),
					tint = Color.White
				)
			}
		}
	}
}
