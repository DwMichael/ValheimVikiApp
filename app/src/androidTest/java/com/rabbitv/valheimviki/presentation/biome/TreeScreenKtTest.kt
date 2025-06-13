//package com.rabbitv.valheimviki.presentation.biome
//
//
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.test.ExperimentalTestApi
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.assertIsNotDisplayed
//import androidx.compose.ui.test.hasTestTag
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.performTouchInput
//import androidx.compose.ui.test.swipe
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.rabbitv.valheimviki.MainActivity
//import com.rabbitv.valheimviki.presentation.biome.viewmodel.BiomeScreenViewModel
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import javax.inject.Inject
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class TreeScreenKtTest {
//    @get:Rule(order = 0)
//    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createAndroidComposeRule<MainActivity>()
//
//    @Inject
//    lateinit var biomeViewModel: BiomeScreenViewModel
//
//    @Before
//    fun setUp() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun showLoadingIndicatorWhenLoadingAndNotShowWhenFalse() {
//        if (biomeViewModel.biomeUIState.value.isLoading) {
//            composeTestRule
//                .onNodeWithTag("LoadingIndicator")
//                .assertExists()
//            composeTestRule
//                .onNodeWithTag("LoadingIndicator")
//                .assertIsDisplayed()
//            composeTestRule.onNodeWithTag("BiomeSurface").assertDoesNotExist()
//        } else {
//            composeTestRule
//                .onNodeWithTag("LoadingIndicator")
//                .assertDoesNotExist()
//            composeTestRule
//                .onNodeWithTag("LoadingIndicator")
//                .assertIsNotDisplayed()
//            composeTestRule.onNodeWithTag("BiomeSurface").assertExists()
//        }
//
//    }
//
//    @OptIn(ExperimentalTestApi::class)
//    @Test
//    fun testContentShownBasedOnBiomeState() {
//        if (biomeViewModel.biomeUIState.value.isLoading) {
//            composeTestRule
//                .onNodeWithTag("LoadingIndicator")
//                .assertExists()
//            composeTestRule
//                .onNodeWithTag("LoadingIndicator")
//                .assertIsDisplayed()
//            composeTestRule.onNodeWithTag("BiomeSurface").assertDoesNotExist()
//        } else {
//            composeTestRule
//                .onNodeWithTag("LoadingIndicator")
//                .assertDoesNotExist()
//            composeTestRule
//                .onNodeWithTag("LoadingIndicator")
//                .assertIsNotDisplayed()
//            composeTestRule.onNodeWithTag("BiomeSurface").assertExists()
//        }
//
//        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)
//
//        composeTestRule.onNodeWithTag("BiomeSurface").assertExists("Expected Surface")
//        composeTestRule.onNodeWithTag("BiomeSurface").assertIsDisplayed()
//
//        if (biomeViewModel.biomeUIState.value.biomes.isEmpty()) {
//
//            composeTestRule.onNodeWithTag("BiomeGrid").assertDoesNotExist()
//            composeTestRule.onNodeWithTag("EmptyScreenBiome").assertExists("Expected Empty Screen")
//            composeTestRule.onNodeWithTag("EmptyScreenBiome").assertIsDisplayed()
//        } else {
//
//            composeTestRule.onNodeWithTag("BiomeGrid").assertExists("Expected Biome Grid")
//            composeTestRule.onNodeWithTag("BiomeGrid").assertIsDisplayed()
//            composeTestRule.onNodeWithTag("EmptyScreenBiome").assertDoesNotExist()
//        }
//    }
//
//    @OptIn(ExperimentalTestApi::class)
//    @Test
//    fun testRefetchBiomes_withPullToRefresh() {
//
//        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)
//
//        composeTestRule.onNodeWithTag("BiomeSurface")
//            .assertExists("Expected BiomeSurface to be displayed")
//            .assertIsDisplayed()
//
//        if (biomeViewModel.biomeUIState.value.biomes.isEmpty()) {
//            composeTestRule.onNodeWithTag("BiomeGrid").assertDoesNotExist()
//            composeTestRule.onNodeWithTag("EmptyScreenBiome")
//                .assertExists("Expected Empty Screen for empty data")
//                .assertIsDisplayed()
//        } else {
//            composeTestRule.onNodeWithTag("BiomeGrid")
//                .assertExists("Expected Biome Grid for non-empty data")
//                .assertIsDisplayed()
//            composeTestRule.onNodeWithTag("EmptyScreenBiome").assertDoesNotExist()
//        }
//
//        val refreshableTag = if (biomeViewModel.biomeUIState.value.biomes.isEmpty()) {
//            "EmptyScreenBiome"
//        } else {
//            "BiomeGrid"
//        }
//
//        composeTestRule.onNodeWithTag(refreshableTag).performTouchInput {
//            val endY = center.y + 800
//            swipe(start = topCenter, end = Offset(center.x, endY), durationMillis = 300)
//        }
//
//
//
//        composeTestRule.waitUntilDoesNotExist(hasTestTag("LoadingIndicator"), timeoutMillis = 5000)
//
//        composeTestRule.onNodeWithTag("BiomeSurface")
//            .assertExists("Expected BiomeSurface after refresh")
//            .assertIsDisplayed()
//
//        if (biomeViewModel.biomeUIState.value.biomes.isEmpty()) {
//            composeTestRule.onNodeWithTag("BiomeGrid").assertDoesNotExist()
//            composeTestRule.onNodeWithTag("EmptyScreenBiome")
//                .assertExists("Expected Empty Screen after refresh")
//                .assertIsDisplayed()
//        } else {
//            composeTestRule.onNodeWithTag("BiomeGrid")
//                .assertExists("Expected Biome Grid after refresh")
//                .assertIsDisplayed()
//            composeTestRule.onNodeWithTag("EmptyScreenBiome").assertDoesNotExist()
//        }
//    }
//
//}