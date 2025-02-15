package com.rabbitv.valheimviki.presentation.creatures

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rabbitv.valheimviki.presentation.biome.CreatureScreen
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CreatureScreenKtTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var creaturesViewModel: CreaturesViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            ValheimVikiAppTheme {
                CreatureScreen(
                    paddingValues = PaddingValues(0.dp),
                    viewModel = creaturesViewModel,
                    navController = NavHostController(LocalContext.current)
                )
            }
        }
    }

    @Test
    fun showLoadingIndicatorWhenLoadingAndNotShowWhenFalse() {

        if (creaturesViewModel.creatureUIState.value.isLoading) {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertExists()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("CreaturesSurface").assertDoesNotExist()
        } else {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertDoesNotExist()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsNotDisplayed()
            composeTestRule.onNodeWithTag("CreaturesSurface").assertExists()
        }

    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentShownBasedOnCreatureState() {
        if (creaturesViewModel.creatureUIState.value.isLoading) {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertExists()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("CreaturesSurface").assertDoesNotExist()
        } else {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertDoesNotExist()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsNotDisplayed()
            composeTestRule.onNodeWithTag("CreaturesSurface").assertExists()
        }

        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)

        composeTestRule.onNodeWithTag("CreaturesSurface").assertExists("Expected Surface")
        composeTestRule.onNodeWithTag("CreaturesSurface").assertIsDisplayed()

        if (creaturesViewModel.creatureUIState.value.creatures.isEmpty()) {

            composeTestRule.onNodeWithTag("CreatureGrid").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenCreatures")
                .assertExists("Expected Empty Screen")
            composeTestRule.onNodeWithTag("EmptyScreenCreatures").assertIsDisplayed()
        } else {

            composeTestRule.onNodeWithTag("CreatureGrid").assertExists("Expected Creature Grid")
            composeTestRule.onNodeWithTag("CreatureGrid").assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenCreatures").assertDoesNotExist()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRefetchCreatureWithPullToRefresh() {

        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)

        composeTestRule.onNodeWithTag("CreaturesSurface")
            .assertExists("Expected CreaturesSurface to be displayed")
            .assertIsDisplayed()

        if (creaturesViewModel.creatureUIState.value.creatures.isEmpty()) {
            composeTestRule.onNodeWithTag("CreatureGrid").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenCreatures")
                .assertExists("Expected Empty Screen for empty data")
                .assertIsDisplayed()
        } else {
            composeTestRule.onNodeWithTag("CreatureGrid")
                .assertExists("Expected Creature Grid for non-empty data")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenCreatures").assertDoesNotExist()
        }

        val refreshableTag = if (creaturesViewModel.creatureUIState.value.creatures.isEmpty()) {
            "EmptyScreenCreatures"
        } else {
            "CreatureGrid"
        }

        composeTestRule.onNodeWithTag(refreshableTag).performTouchInput {
            val endY = center.y + 800
            swipe(start = topCenter, end = Offset(center.x, endY), durationMillis = 300)
        }

        composeTestRule.waitUntil(timeoutMillis = 1000) {
            composeTestRule.onAllNodes(hasTestTag("LoadingIndicator")).fetchSemanticsNodes()
                .isNotEmpty()
        }


        composeTestRule.onNodeWithTag("LoadingIndicator")
            .assertExists("Loading indicator should appear during refresh")
            .assertIsDisplayed()

        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)

        composeTestRule.onNodeWithTag("CreaturesSurface")
            .assertExists("Expected CreaturesSurface after refresh")
            .assertIsDisplayed()

        if (creaturesViewModel.creatureUIState.value.creatures.isEmpty()) {
            composeTestRule.onNodeWithTag("CreatureGrid").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenCreatures")
                .assertExists("Expected Empty Screen after refresh")
                .assertIsDisplayed()
        } else {
            composeTestRule.onNodeWithTag("CreatureGrid")
                .assertExists("Expected Creature Grid after refresh")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenCreatures").assertDoesNotExist()
        }
    }
}