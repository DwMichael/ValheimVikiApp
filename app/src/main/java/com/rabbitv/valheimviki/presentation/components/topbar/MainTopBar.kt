package com.rabbitv.valheimviki.presentation.components.topbar

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.ICON_CLICK_DIM
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
        onSearchBarClick: () -> Unit = {},
        onBookMarkClick: () -> Unit = {},
        settingsClick: () -> Unit = {},
        scope: CoroutineScope,
        drawerState: DrawerState,
        enabled: () -> Boolean = { true },
        highlightSettings: Boolean = false
) {
    TopAppBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            colors =
                    TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
            modifier = Modifier.testTag("HomeTopAppBar"),
            navigationIcon = {
                IconButton(
                        enabled = !enabled(),
                        onClick = { scope.launch { drawerState.open() } },
                        modifier = Modifier.size(ICON_CLICK_DIM)
                ) {
                    Icon(
                            painter = painterResource(id = R.drawable.bars),
                            contentDescription = stringResource(R.string.cd_menu_icon),
                            modifier = Modifier.size(ICON_SIZE),
                            tint =
                                    if (highlightSettings)
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            title = {
                Text(
                        stringResource(R.string.app_name),
                        color =
                                if (highlightSettings)
                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                                alpha = 0.2f
                                        )
                                else MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineMedium,
                )
            },
            actions = {
                IconButton(
                        onClick = { onBookMarkClick() },
                        modifier = Modifier.testTag("nav_favorites").size(ICON_CLICK_DIM)
                ) {
                    Icon(
                            painter = painterResource(R.drawable.ic_bookmarks),
                            contentDescription = stringResource(R.string.cd_bookmarks_icon),
                            modifier = Modifier.size(ICON_SIZE),
                            tint =
                                    if (highlightSettings)
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(
                        onClick = { onSearchBarClick() },
                        modifier = Modifier.size(ICON_CLICK_DIM)
                ) {
                    Icon(
                            painter = painterResource(R.drawable.icon_search),
                            contentDescription = stringResource(R.string.cd_search_icon),
                            modifier = Modifier.size(ICON_SIZE),
                            tint =
                                    if (highlightSettings)
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(
                        onClick = { settingsClick() },
                        modifier =
                                Modifier.testTag("nav_settings")
                                        .size(ICON_CLICK_DIM)
                                        .graphicsLayer {
                                            if (highlightSettings) {
                                                this.alpha = 1f
                                                this.scaleX = 1.2f
                                                this.scaleY = 1.2f
                                            }
                                        }
                                        .then(
                                                if (highlightSettings) {
                                                    Modifier.background(
                                                            Color(0xFFDD6119).copy(alpha = 0.5f),
                                                            CircleShape
                                                    )
                                                } else {
                                                    Modifier
                                                }
                                        )
                ) {
                    Icon(
                            imageVector = Lucide.Settings,
                            contentDescription = stringResource(R.string.settings),
                            modifier = Modifier.size(ICON_SIZE),
                            tint =
                                    if (highlightSettings) Color.White
                                    else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
    )
}

@Preview(name = "HomeTopBar", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewHomeTopBar() {
    ValheimVikiAppTheme { // Ensure your app theme is applied
        MainAppBar(
                onSearchBarClick = {},
                onBookMarkClick = {},
                settingsClick = {},
                scope = rememberCoroutineScope(),
                drawerState = rememberDrawerState(DrawerValue.Closed)
        )
    }
}
