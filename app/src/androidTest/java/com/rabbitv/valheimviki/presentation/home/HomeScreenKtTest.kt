package com.rabbitv.valheimviki.presentation.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rabbitv.valheimviki.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenKtTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testHomeScreenDisplaysDrawerAndContent() {

        composeTestRule.onNodeWithTag("NavigationDrawer")
            .assertExists("Navigation drawer should exist on the HomeScreen")

        composeTestRule.onNodeWithTag("HomeContent")
            .assertExists("Home content should be displayed on the HomeScreen")
            .assertIsDisplayed()
        
    }
}