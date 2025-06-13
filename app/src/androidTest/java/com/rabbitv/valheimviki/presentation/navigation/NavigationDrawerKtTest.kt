//package com.rabbitv.valheimviki.presentation.navigation
//
//import androidx.compose.material3.DrawerValue
//import androidx.compose.material3.rememberDrawerState
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.navigation.compose.rememberNavController
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.composables.icons.lucide.Lucide
//import com.composables.icons.lucide.MountainSnow
//import com.composables.icons.lucide.Rabbit
//import com.rabbitv.valheimviki.R
//import com.rabbitv.valheimviki.navigation.Screen
//import com.rabbitv.valheimviki.presentation.components.DrawerItem
//import com.rabbitv.valheimviki.presentation.components.NavigationDrawer
//import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class NavigationDrawerKtTest {
//    @get:Rule(order = 0)
//    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createComposeRule()
//
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//        composeTestRule.setContent {
//
//            val items = listOf(
//                DrawerItem(
//                    icon = Lucide.MountainSnow,
//                    label = "Biomes",
//                    contentDescription = "List of Biomes",
//                    route = Screen.Biome.route
//                ),
//                DrawerItem(
//                    iconPainter = painterResource(R.drawable.skull),
//                    label = "Bosses",
//                    contentDescription = "Creatures section",
//                    route = Screen.Boss.route
//                ),
//                DrawerItem(
//                    iconPainter = painterResource(R.drawable.ogre),
//                    label = "MiniBosses",
//                    contentDescription = "Creatures section",
//                    route = Screen.MiniBoss.route
//                ),
//                DrawerItem(
//                    icon = Lucide.Rabbit,
//                    label = "Creatures",
//                    contentDescription = "Creatures section",
//                    route = Screen.Creature.route
//                ),
//
//                )
//            val selectedItem: MutableState<DrawerItem> = remember { mutableStateOf(items[0]) }
//            ValheimVikiAppTheme {
//                NavigationDrawer(
//                    modifier = Modifier,
//                    drawerState = rememberDrawerState(DrawerValue.Open),
//                    scope = rememberCoroutineScope(),
//                    childNavController = rememberNavController(),
//                    items = items,
//                    selectedItem = selectedItem,
//                    content = {}
//                )
//            }
//        }
//    }
//
//    @Test
//    fun testHomeScreenDisplaysDrawerAndContent() {
//
//        composeTestRule.onNodeWithTag("NavigationDrawer")
//            .assertExists("Navigation drawer should exist on the HomeScreen")
//            .assertIsDisplayed()
//
//
//        composeTestRule.onNodeWithText("Biomes")
//            .assertExists("The drawer should display the 'Biomes' menu item")
//            .assertIsDisplayed()
//
//        composeTestRule.onNodeWithText("Bosses")
//            .assertExists("The drawer should display the 'Bosses' menu item")
//            .assertIsDisplayed()
//
//        composeTestRule.onNodeWithText("MiniBosses")
//            .assertExists("The drawer should display the 'MiniBosses' menu item")
//            .assertIsDisplayed()
//
//        composeTestRule.onNodeWithText("Creatures")
//            .assertExists("The drawer should display the 'Creatures' menu item")
//            .assertIsDisplayed()
//    }
//
//}
