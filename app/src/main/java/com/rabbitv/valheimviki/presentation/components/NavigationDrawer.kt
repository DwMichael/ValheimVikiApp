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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MountainSnow
import com.composables.icons.lucide.Rabbit
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.Screen
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
    val route: String = label
)

@Composable
fun NavigationDrawer(
    modifier: Modifier,
    drawerState: DrawerState,
    scope: CoroutineScope,
    childNavController: NavHostController,
    items: List<DrawerItem>,
    selectedItem: MutableState<DrawerItem>,
    isDetailScreen: Boolean,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(

        modifier = modifier
            .fillMaxSize()
            .testTag("NavigationDrawer"),
        drawerState = drawerState,
        gesturesEnabled = !isDetailScreen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.92f),
                drawerContainerColor = ForestGreen40Dark,
            ) {
                Column(
                    Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 0.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start

                    ) {
                        Image(
                            modifier = Modifier
                                .size(42.dp),
                            painter = painterResource(R.drawable.viking),
                            contentDescription = "DrawerLogoImage",
                            contentScale = ContentScale.FillBounds,
                        )
                        Spacer(modifier.padding(12.dp))
                        Text(
                            text = "ValheimViki",
                            fontWeight = FontWeight.Medium,
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(12.dp))
                    items.forEach { item ->
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
                            selected = (item == selectedItem.value),
                            onClick = {
                                childNavController.navigate(item.route) {
                                    popUpTo(childNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .padding(
                                    NavigationDrawerItemDefaults
                                        .ItemPadding
                                )
                        )
                    }
                }
            }
        },
    ) {
        content()
    }
}

@Preview(name = "NavigationDrawerImage", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationDrawerImage() {

    ValheimVikiAppTheme {

        Image(
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp, end = 12.dp)
                .size(80.dp),
            painter = painterResource(R.drawable.viking),
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
            route = Screen.Biome.route
        ),
        DrawerItem(
            iconPainter = painterResource(R.drawable.skull),
            label = "Bosses",
            contentDescription = "Bosses section",
            route = Screen.Boss.route
        ),
        DrawerItem(
            iconPainter = painterResource(R.drawable.ogre),
            label = "MiniBosses",
            contentDescription = "MiniBosses section",
            route = Screen.MiniBoss.route
        ),
        DrawerItem(
            icon = Lucide.Rabbit,
            label = "Creatures",
            contentDescription = "Creatures section",
            route = ""
        ),

        )
    ValheimVikiAppTheme {
        NavigationDrawer(
            modifier = Modifier,
            drawerState = rememberDrawerState(DrawerValue.Open),
            scope = rememberCoroutineScope(),
            childNavController = rememberNavController(),
            items = items,
            selectedItem = remember { mutableStateOf(items[0]) },
            content = {},
            isDetailScreen = false
        )
    }
}