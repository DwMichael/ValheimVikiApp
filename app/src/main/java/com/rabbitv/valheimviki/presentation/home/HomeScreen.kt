package com.rabbitv.valheimviki.presentation.home


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MountainSnow
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.ChildNavGraph
import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.presentation.navigation.DrawerItem
import com.rabbitv.valheimviki.presentation.navigation.NavigationDrawer
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    childNavController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = getDrawerItems()
    val selectedItem: MutableState<DrawerItem> = remember { mutableStateOf(items[0]) }
    val navBackStackEntry by childNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val isDetailScreen = currentRoute?.let { route ->
        route.startsWith(Screen.BiomeDetail.route.substringBefore("{")) ||
                route.startsWith(Screen.CreatureDetail.route.substringBefore("{"))
    } == true

    LaunchedEffect(currentRoute) {
        if (!isDetailScreen) {
            val item = items.find { it.route == currentRoute }
            if (item != null) {
                selectedItem.value = item
            }
        }else{
            val kolo = DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        }
    }

    NavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        scope = scope,
        childNavController = childNavController,
        items = items,
        selectedItem = selectedItem,
        isDetailScreen = isDetailScreen
    ) {

        Box(
            modifier = Modifier
                .testTag("HomeContent")
                .fillMaxSize()
        ) {
            if (isDetailScreen) {
                Image(
                    painter = painterResource(id = R.drawable.main_background),
                    contentDescription = "BackgroundImage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                ChildNavGraph(
                    paddingValues = PaddingValues(0.dp),
                    navHostController = childNavController
                )
            } else {
                HomeContent(
                    drawerState = drawerState,
                    scope = scope,
                    childNavController = childNavController,
                )
            }
        }
    }
}


@Composable
private fun getDrawerItems():List<DrawerItem>{
   return listOf(
       DrawerItem(
           icon = Lucide.MountainSnow,
           label = stringResource(R.string.biomes),
           contentDescription = stringResource(R.string.biomes_section),
           route = Screen.Biome.route
       ),
       DrawerItem(
           iconPainter = painterResource(R.drawable.skull),
           label = stringResource(R.string.bosses),
           contentDescription = stringResource(R.string.boss_section),
           route = Screen.Boss.route
       ),
       DrawerItem(
           iconPainter = painterResource(R.drawable.ogre),
           label = stringResource(R.string.minibosses),
           contentDescription = stringResource(R.string.minibosses_section),
           route = Screen.MiniBoss.route
       ),
   )
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
