package com.rabbitv.valheimviki.presentation

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MountainSnow
import com.composables.icons.lucide.Rabbit
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.ChildNavGraph
import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.presentation.base.DrawerItem
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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
            contentDescription = "Biome section",
            route = Screen.Biome.route
        ),
        DrawerItem(
            icon = Lucide.Rabbit,
            label = "Creatures",
            contentDescription = "Creatures section",
            route = Screen.Creature.route
        )
    )
    val selectedItem: MutableState<DrawerItem> = remember { mutableStateOf(items[0]) }

    NavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        scope = scope,
        childNavController = childNavController,
        items = items,
        selectedItem = selectedItem
    ){
        HomeScreenContent(
            item=selectedItem.value.label,
            drawerState = drawerState,
            scope = scope,
            childNavController = childNavController,
        )
    }
}


@Composable
fun NavigationDrawer(
    modifier: Modifier ,
    drawerState: DrawerState,
    scope: CoroutineScope,
    childNavController: NavHostController,
    items: List<DrawerItem>,
    selectedItem : MutableState<DrawerItem>,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier.fillMaxSize(),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Spacer(Modifier.height(12.dp))
                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.contentDescription
                                )
                            },
                            label = { Text(item.label) },
                            selected = (item == selectedItem.value),
                            onClick = {
                                childNavController.navigate(item.route) {
                                    popUpTo(childNavController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                selectedItem.value = item
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        },
    ) {
        content()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    item: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    childNavController: NavHostController
){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            painter = painterResource(id = R.drawable.bars),
                            contentDescription = "Menu section",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                title = { Text("ValheimViki") }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(item, fontFamily = MaterialTheme.typography.bodyLarge.fontFamily, fontSize = 48.sp)
                }
                ChildNavGraph(navHostController = childNavController)
            }

        }
    )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun PreviewHomeScreenContent() {

    val items = listOf(
        DrawerItem(
            icon = Lucide.MountainSnow,
            label = "Biomes",
            contentDescription = "Biome section",
            route = Screen.Biome.route
        ),
        DrawerItem(
            icon = Lucide.Rabbit,
            label = "Creatures",
            contentDescription = "Creatures section",
            route = Screen.Creature.route
        )
    )


    CompositionLocalProvider(
    ) {
        ValheimVikiAppTheme {
            HomeScreenContent(
                drawerState = DrawerState(DrawerValue.Closed),
                scope = rememberCoroutineScope(),
                childNavController = rememberNavController(),
                item = "Tittle"
            )
        }
    }

}
