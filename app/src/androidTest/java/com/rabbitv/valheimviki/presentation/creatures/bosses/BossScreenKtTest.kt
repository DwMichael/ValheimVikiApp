package com.rabbitv.valheimviki.presentation.creatures.bosses

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
import com.rabbitv.valheimviki.presentation.creatures.bosses.viewmodel.BossesViewModel
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
class BossScreenKtTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var bossViewModel: BossesViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            ValheimVikiAppTheme {
                BossScreen(
                    paddingValues = PaddingValues(0.dp),
                    viewModel = bossViewModel,
                    navController = NavHostController(LocalContext.current)
                )
            }
        }
    }

    @Test
    fun showLoadingIndicatorWhenLoadingAndNotShowWhenFalse() {

        if (bossViewModel.bossUIState.value.isLoading) {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertExists()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("BossSurface").assertDoesNotExist()
        } else {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertDoesNotExist()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsNotDisplayed()
            composeTestRule.onNodeWithTag("BossSurface").assertExists()
        }

    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentShownBasedOnBossState() {
        if (bossViewModel.bossUIState.value.isLoading) {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertExists()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("BossSurface").assertDoesNotExist()
        } else {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertDoesNotExist()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsNotDisplayed()
            composeTestRule.onNodeWithTag("BossSurface").assertExists()
        }

        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)

        composeTestRule.onNodeWithTag("BossSurface").assertExists("Expected Surface")
        composeTestRule.onNodeWithTag("BossSurface").assertIsDisplayed()

        if (bossViewModel.bossUIState.value.bosses.isEmpty()) {

            composeTestRule.onNodeWithTag("BossGrid").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenBoss").assertExists("Expected Empty Screen")
            composeTestRule.onNodeWithTag("EmptyScreenBoss").assertIsDisplayed()
        } else {

            composeTestRule.onNodeWithTag("BossGrid").assertExists("Expected Boss Grid")
            composeTestRule.onNodeWithTag("BossGrid").assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenBoss").assertDoesNotExist()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRefetchBossWithPullToRefresh() {

        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)

        composeTestRule.onNodeWithTag("BossSurface")
            .assertExists("Expected BossSurface to be displayed")
            .assertIsDisplayed()

        if (bossViewModel.bossUIState.value.bosses.isEmpty()) {
            composeTestRule.onNodeWithTag("BossGrid").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenBoss")
                .assertExists("Expected Empty Screen for empty data")
                .assertIsDisplayed()
        } else {
            composeTestRule.onNodeWithTag("BossGrid")
                .assertExists("Expected Boss Grid for non-empty data")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenBoss").assertDoesNotExist()
        }

        val refreshableTag = if (bossViewModel.bossUIState.value.bosses.isEmpty()) {
            "EmptyScreenBoss"
        } else {
            "BossGrid"
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

        composeTestRule.onNodeWithTag("BossSurface")
            .assertExists("Expected BossSurface after refresh")
            .assertIsDisplayed()

        if (bossViewModel.bossUIState.value.bosses.isEmpty()) {
            composeTestRule.onNodeWithTag("BossGrid").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenBoss")
                .assertExists("Expected Empty Screen after refresh")
                .assertIsDisplayed()
        } else {
            composeTestRule.onNodeWithTag("BossGrid")
                .assertExists("Expected Boss Grid after refresh")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenBoss").assertDoesNotExist()
        }
    }
}