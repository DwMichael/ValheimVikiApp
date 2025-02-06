package com.rabbitv.valheimviki.presentation.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MountainSnow
import com.composables.icons.lucide.Rabbit
import com.rabbitv.valheimviki.navigation.Screen
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    modifier: Modifier,
    drawerState: DrawerState,
    scope: CoroutineScope,
    childNavController: NavHostController,
    items: List<DrawerItem>,
    selectedItem: MutableState<DrawerItem>,
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
@Preview(name = "NavigationDrawer", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNavigationDrawer() {

    val items = listOf(
        DrawerItem(
            icon = Lucide.MountainSnow,
            label = "Biomes",
            contentDescription = "List of Biomes",
            route = Screen.BiomeList.route
        ),
        DrawerItem(
            icon = Lucide.Rabbit,
            label = "Creatures",
            contentDescription = "Creatures section",
            route = Screen.CreatureList.route
        )
    )
    ValheimVikiAppTheme {
        NavigationDrawer(
            modifier = Modifier,
            drawerState = rememberDrawerState(DrawerValue.Open),
            scope = rememberCoroutineScope(),
            childNavController = rememberNavController(),
            items = items,
            selectedItem = remember { mutableStateOf(items[0]) },
            content = {}
        )
    }
}