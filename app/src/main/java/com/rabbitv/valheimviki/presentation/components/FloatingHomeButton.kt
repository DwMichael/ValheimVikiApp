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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
	var lastDrawerDestinationId by rememberSaveable { mutableStateOf<Int?>(null) }

	LaunchedEffect(current) {
		val dest = current?.destination
		if (dest?.shouldShowTopBar() == true) {
			lastDrawerDestinationId = dest.id
		}
	}

	val prevDest = navController.previousBackStackEntry?.destination
	val shouldShow = (current?.destination?.shouldShowTopBar() == false) &&
			(prevDest?.shouldShowTopBar() == false)

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
					val poppedToDrawer = lastDrawerDestinationId?.let {
						navController.popBackStack(it, inclusive = false)
					} ?: false

					if (!poppedToDrawer) {
						val startDest = navController.graph.findStartDestination()
						val poppedToStart =
							navController.popBackStack(startDest.id, inclusive = false)
						if (!poppedToStart) {
							startDest.route?.let { startRoute ->
								navController.navigate(startRoute) {
									popUpTo(startDest.id) { saveState = true }
									launchSingleTop = true
									restoreState = true
								}
							}
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
