package com.rabbitv.valheimviki.presentation.creatures.mini_miniBosses

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
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossScreen
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.viewmodel.MiniBossesViewModel
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
class MiniBossScreenKtTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var miniBossViewModel: MiniBossesViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            ValheimVikiAppTheme {
                MiniBossScreen(
                    paddingValues = PaddingValues(0.dp),
                    viewModel = miniBossViewModel,
                    navController = NavHostController(LocalContext.current)
                )
            }
        }
    }

    @Test
    fun showLoadingIndicatorWhenLoadingAndNotShowWhenFalse() {

        if (miniBossViewModel.miniBossesUIState.value.isLoading) {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertExists()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("MiniBossSurface").assertDoesNotExist()
        } else {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertDoesNotExist()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsNotDisplayed()
            composeTestRule.onNodeWithTag("MiniBossSurface").assertExists()
        }

    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentShownBasedOnMiniBossState() {
        if (miniBossViewModel.miniBossesUIState.value.isLoading) {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertExists()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("MiniBossSurface").assertDoesNotExist()
        } else {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertDoesNotExist()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsNotDisplayed()
            composeTestRule.onNodeWithTag("MiniBossSurface").assertExists()
        }

        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)

        composeTestRule.onNodeWithTag("MiniBossSurface").assertExists("Expected Surface")
        composeTestRule.onNodeWithTag("MiniBossSurface").assertIsDisplayed()

        if (miniBossViewModel.miniBossesUIState.value.miniBosses.isEmpty()) {

            composeTestRule.onNodeWithTag("MiniBossGird").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenCreatures")
                .assertExists("Expected Empty Screen")
            composeTestRule.onNodeWithTag("EmptyScreenMiniBoss").assertIsDisplayed()
        } else {

            composeTestRule.onNodeWithTag("MiniBossGird").assertExists("Expected MiniBoss Grid")
            composeTestRule.onNodeWithTag("MiniBossGird").assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenMiniBoss").assertDoesNotExist()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRefetchMiniBossWithPullToRefresh() {

        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)

        composeTestRule.onNodeWithTag("MiniBossSurface")
            .assertExists("Expected MiniBossSurface to be displayed")
            .assertIsDisplayed()

        if (miniBossViewModel.miniBossesUIState.value.miniBosses.isEmpty()) {
            composeTestRule.onNodeWithTag("MiniBossGird").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenMiniBoss")
                .assertExists("Expected Empty Screen for empty data")
                .assertIsDisplayed()
        } else {
            composeTestRule.onNodeWithTag("MiniBossGird")
                .assertExists("Expected MiniBoss Grid for non-empty data")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenMiniBoss").assertDoesNotExist()
        }

        val refreshableTag = if (miniBossViewModel.miniBossesUIState.value.miniBosses.isEmpty()) {
            "EmptyScreenMiniBoss"
        } else {
            "MiniBossGird"
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

        composeTestRule.onNodeWithTag("MiniBossSurface")
            .assertExists("Expected MiniBossSurface after refresh")
            .assertIsDisplayed()

        if (miniBossViewModel.miniBossesUIState.value.miniBosses.isEmpty()) {
            composeTestRule.onNodeWithTag("MiniBossGird").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenMiniBoss")
                .assertExists("Expected Empty Screen after refresh")
                .assertIsDisplayed()
        } else {
            composeTestRule.onNodeWithTag("MiniBossGird")
                .assertExists("Expected MiniBoss Grid after refresh")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenMiniBoss").assertDoesNotExist()
        }
    }
}