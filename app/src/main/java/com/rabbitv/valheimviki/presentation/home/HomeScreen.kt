package com.rabbitv.valheimviki.presentation.home


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MountainSnow
import com.composables.icons.lucide.Rabbit
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.presentation.navigation.DrawerItem
import com.rabbitv.valheimviki.presentation.navigation.NavigationDrawer
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val childNavController = rememberNavController()

    val items = listOf(
        DrawerItem(
            icon = Lucide.MountainSnow,
            label = "Biomes",
            contentDescription = "List of Biomes",
            route = Screen.BiomeList.route
        ),
        DrawerItem(
            iconPainter = painterResource(R.drawable.skull),
            label = "Bosses",
            contentDescription = "Creatures section",
            route = Screen.Boss.route
        ),
        DrawerItem(
            iconPainter = painterResource(R.drawable.ogre),
            label = "MiniBosses",
            contentDescription = "Creatures section",
            route = Screen.MiniBoss.route
        ),
        DrawerItem(
            icon = Lucide.Rabbit,
            label = "Creatures",
            contentDescription = "Creatures section",
            route = Screen.CreatureList.route
        ),

        )
    val selectedItem: MutableState<DrawerItem> = remember { mutableStateOf(items[0]) }

    NavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        scope = scope,
        childNavController = childNavController,
        items = items,
        selectedItem = selectedItem
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            HomeContent(
                drawerState = drawerState,
                scope = scope,
                childNavController = childNavController,
            )
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun PreviewHomeScreenContent() {


    CompositionLocalProvider {
        ValheimVikiAppTheme {
            HomeContent(
                drawerState = DrawerState(DrawerValue.Closed),
                scope = rememberCoroutineScope(),
                childNavController = rememberNavController(),
            )
        }
    }

}
