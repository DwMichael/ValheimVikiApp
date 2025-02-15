package com.rabbitv.valheimviki.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.pressKey
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

    @OptIn(ExperimentalTestApi::class)
    private fun navigateGoBackToBiomeScreen() {
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.Back)
        }
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
        navController.assertCurrentRouteName(Screen.Biome.route)
    }

    @Test
    fun navigateFromDrawerToBossScreen() {
        composeTestRule.onNodeWithContentDescription("Menu section Icon").performClick()
        composeTestRule.onNodeWithTag("NavigationDrawer").isDisplayed()

        assertWithTextIfExist(R.string.biomes, "Expected Biomes Button")
        assertWithTextIfExist(R.string.bosses, "Expected Bosses Button")
        assertWithTextIfExist(R.string.minibosses, "Expected MiniBosses Button")
        assertWithTextIfExist(R.string.creatures, "Expected Creatures Button")

        navigateToBossesScreen()

        navController.assertCurrentRouteName(Screen.Boss.route)
    }

    @Test
    fun navigateFromDrawerToMiniBossScreen() {
        composeTestRule.onNodeWithContentDescription("Menu section Icon").performClick()
        composeTestRule.onNodeWithTag("NavigationDrawer").isDisplayed()

        assertWithTextIfExist(R.string.biomes, "Expected Biomes Button")
        assertWithTextIfExist(R.string.bosses, "Expected Bosses Button")
        assertWithTextIfExist(R.string.minibosses, "Expected MiniBosses Button")
        assertWithTextIfExist(R.string.creatures, "Expected Creatures Button")

        navigateToMiniBossesScreen()

        navController.assertCurrentRouteName(Screen.MiniBoss.route)
    }

    @Test
    fun navigateFromDrawerToCreaturesScreen() {
        composeTestRule.onNodeWithContentDescription("Menu section Icon").performClick()
        composeTestRule.onNodeWithTag("NavigationDrawer").isDisplayed()

        assertWithTextIfExist(R.string.biomes, "Expected Biomes Button")
        assertWithTextIfExist(R.string.bosses, "Expected Bosses Button")
        assertWithTextIfExist(R.string.minibosses, "Expected MiniBosses Button")
        assertWithTextIfExist(R.string.creatures, "Expected Creatures Button")

        navigateToCreaturesScreen()

        navController.assertCurrentRouteName(Screen.Creature.route)
    }


    @Test
    fun navigateByDrawerBackToBiomeScreen() {
        composeTestRule.onNodeWithContentDescription("Menu section Icon").performClick()
        composeTestRule.onNodeWithTag("NavigationDrawer").isDisplayed()

        assertWithTextIfExist(R.string.biomes, "Expected Biomes Button")
        assertWithTextIfExist(R.string.bosses, "Expected Bosses Button")
        assertWithTextIfExist(R.string.minibosses, "Expected MiniBosses Button")
        assertWithTextIfExist(R.string.creatures, "Expected Creatures Button")

        navigateToCreaturesScreen()

        navController.assertCurrentRouteName(Screen.Creature.route)

        composeTestRule.onNodeWithContentDescription("Menu section Icon").performClick()
        composeTestRule.onNodeWithTag("NavigationDrawer").isDisplayed()

        navigateToBiomesScreen()

        navController.assertCurrentRouteName(Screen.Biome.route)

    }


    @Test
    fun navigateIntoBiomeDetailScreen() {
        navController.assertCurrentRouteName(Screen.Biome.route)

        composeTestRule.onNodeWithTag("BiomeGrid").assertExists().isDisplayed()
        composeTestRule.onNodeWithTag("GirdItem Meadows").assertExists().isDisplayed()

        composeTestRule.onNodeWithTag("GirdItem Meadows").performClick()

        navController.assertCurrentRouteName(Screen.BiomeDetail.route)


    }

}