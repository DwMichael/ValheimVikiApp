package com.rabbitv.valheimviki.presentation.home

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rabbitv.valheimviki.ui.theme.ICON_CLICK_DIM
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeTopBarKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            ValheimVikiAppTheme {
                HomeTopBar(
                    onSearchBarClick = {},
                    onMenuClick = {},
                    onBookMarkClick = {},
                    scope = rememberCoroutineScope(),
                    drawerState = rememberDrawerState(DrawerValue.Closed),
                )
            }
        }
    }

    @Test
    fun testHomeTopAppBarIsDisplayed() {
        composeTestRule.onNodeWithTag("HomeTopAppBar").assertIsDisplayed()
    }

    @Test
    fun testHomeTopAppBarIconsAreDisplayed() {
        composeTestRule.onNodeWithContentDescription("Menu section Icon").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Bookmarks section Icon").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Search section Icon").assertIsDisplayed()
    }

    @Test
    fun testIconButtonPaddingAndSize() {
        composeTestRule.onNodeWithContentDescription("Menu section Icon")
            .assertWidthIsEqualTo(ICON_CLICK_DIM).assertHeightIsEqualTo(ICON_CLICK_DIM)
        composeTestRule.onNodeWithContentDescription("Bookmarks section Icon")
            .assertWidthIsEqualTo(ICON_CLICK_DIM).assertHeightIsEqualTo(ICON_CLICK_DIM)
        composeTestRule.onNodeWithContentDescription("Search section Icon")
            .assertWidthIsEqualTo(ICON_CLICK_DIM).assertHeightIsEqualTo(ICON_CLICK_DIM)
    }

    @Test
    fun testHomeTopAppBarTitleIsDisplayed() {
        composeTestRule.onNodeWithText("ValheimViki")
    }
    
}