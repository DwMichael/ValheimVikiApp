package com.rabbitv.valheimviki.presentation.biome


import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rabbitv.valheimviki.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BiomeGridScreenKtTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var biomeViewModel: BiomeGridScreenViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun showLoadingIndicatorWhenLoadingAndDontShowWhenFalse() {
        if (biomeViewModel.biomeUIState.value.isLoading) {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertExists()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsDisplayed()
            composeTestRule.onNodeWithTag("BiomeSurface").assertDoesNotExist()
        } else {
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertDoesNotExist()
            composeTestRule
                .onNodeWithTag("LoadingIndicator")
                .assertIsNotDisplayed()
            composeTestRule.onNodeWithTag("BiomeSurface").assertExists()
        }

    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentShownBasedOnBiomeState() {
        composeTestRule
            .onNodeWithTag("LoadingIndicator")
            .assertExists()
        composeTestRule
            .onNodeWithTag("LoadingIndicator")
            .assertIsDisplayed()

        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)

        composeTestRule.onNodeWithTag("BiomeSurface").assertExists("Expected Surface")
        composeTestRule.onNodeWithTag("BiomeSurface").assertIsDisplayed()

        if (biomeViewModel.biomeUIState.value.biomes.isEmpty()) {

            composeTestRule.onNodeWithTag("BiomeGird").assertDoesNotExist()
            composeTestRule.onNodeWithTag("EmptyScreenBiome").assertExists("Expected Empty Screen")
            composeTestRule.onNodeWithTag("EmptyScreenBiome").assertIsDisplayed()
        } else {

            composeTestRule.onNodeWithTag("BiomeGird").assertExists("Expected Biome Grid")
            composeTestRule.onNodeWithTag("BiomeGird").assertIsDisplayed()
            composeTestRule.onNodeWithTag("EmptyScreenBiome").assertDoesNotExist()
        }
    }


}