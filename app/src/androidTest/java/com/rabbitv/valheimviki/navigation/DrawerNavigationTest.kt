package com.rabbitv.valheimviki.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rabbitv.valheimviki.MainActivity
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.assertions.assertCurrentRouteName
import com.rabbitv.valheimviki.onNodeWithStringId
import com.rabbitv.valheimviki.presentation.home.HomeScreen
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

    private fun assertWithTextIfExist(stringValue: Int, errorText: String) {
        composeTestRule.onNodeWithStringId(stringValue).assertExists(
            errorText
        )
    }

    private fun navigateToBiomesScreen() {
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.biomes_section))
            .performClick()

    }

    private fun navigateToBossesScreen() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(
                R.string
                    .boss_section
            )
        ).performClick()
    }

    private fun navigateToMiniBossesScreen() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(
                R.string
                    .minibosses_section
            )
        ).performClick()
    }

    private fun navigateToCreaturesScreen() {
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(
                R.string
                    .creatures_section
            )
        ).performClick()
    }


    @Before
    fun setUp() {
        composeTestRule.activity.runOnUiThread {
            composeTestRule.activity.setContent {
                val testNavController = TestNavHostController(LocalContext.current).apply {
                    navigatorProvider.addNavigator(ComposeNavigator())
                }
                navController = testNavController

                ValheimVikiAppTheme {
                    HomeScreen(
                        modifier = Modifier,
                        childNavController = testNavController
                    )
                }
            }
        }
        composeTestRule.waitForIdle()
    }


    @Test
    fun verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.BiomeList.route)
    }

    @Test
    fun testDrawerNavigationFromBiomeScreenToBossScreen() {
        composeTestRule.onNodeWithContentDescription("Menu section Icon").performClick()
        composeTestRule.onNodeWithTag("NavigationDrawer").isDisplayed()

        assertWithTextIfExist(R.string.biomes, "Expected Biomes Button")
        assertWithTextIfExist(R.string.bosses, "Expected Bosses Button")
        assertWithTextIfExist(R.string.minibosses, "Expected MiniBosses Button")
        assertWithTextIfExist(R.string.creatures, "Expected Creatures Button")

        navigateToBossesScreen()

        navController.assertCurrentRouteName(Screen.Boss.route)
    }


}