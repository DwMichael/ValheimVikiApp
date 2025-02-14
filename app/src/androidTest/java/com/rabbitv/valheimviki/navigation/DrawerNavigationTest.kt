package com.rabbitv.valheimviki.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DrawerNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.activity.runOnUiThread {
            composeTestRule.activity.setContent {
                val testNavController = TestNavHostController(LocalContext.current).apply {
                    navigatorProvider.addNavigator(ComposeNavigator())
                }
                navController = testNavController

                ValheimVikiAppTheme {
                    SetupNavGraph(navController = testNavController)
                }
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

}
//    @Test
//    fun testDrawerNavigationFromBiomeScreenToBiomeDetail() {
//
//        val navController =
//            TestNavHostController(composeTestRule.activity) // Create TestNavHostController
//
//        composeTestRule.setContent { // Set content and pass the TestNavHostController
//            SetupNavGraph(navController = navController) // Pass it to your NavGraph setup
//            //  If you have a root composable that wraps SetupNavGraph, use that instead.
//            //  For example:  MyApp(navController = navController)
//        }
//
//
//        composeTestRule.onNodeWithContentDescription("Menu section Icon").performClick()
//        composeTestRule.onNodeWithTag("NavigationDrawer").isDisplayed()
//
//        val context = composeTestRule.activity
//
//        composeTestRule.onNodeWithText(context.getString(R.string.biomes)).assertExists(
//            "Expected Biomes Button"
//        )
//        composeTestRule.onNodeWithText(context.getString(R.string.creatures)).assertExists(
//            "Expected Creatures Button"
//        )
//        composeTestRule.onNodeWithText(context.getString(R.string.bosses)).assertExists(
//            "Expected Bosses Button"
//        )
//        composeTestRule.onNodeWithText(context.getString(R.string.minibosses)).assertExists(
//            "Expected MiniBosses Button"
//        )
//
//        composeTestRule.onNodeWithContentDescription(context.getString(R.string.boss_section))
//            .performClick()
//
//        composeTestRule.runOnIdle {
//            assertEquals(Screen.Boss.route, navController.currentBackStackEntry?.destination?.route)
//            // Now it should work because navController is connected to NavHost in setContent
//        }
//    }
//}