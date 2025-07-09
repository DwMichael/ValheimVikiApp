package com.rabbitv.valheimviki.presentation.home

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.ICON_CLICK_DIM
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    onSearchBarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onBookMarkClick: () -> Unit = {},
    scope: CoroutineScope,
    drawerState: DrawerState,
    enabled: Boolean = true
) {
    TopAppBar(
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.testTag("HomeTopAppBar"),
        navigationIcon = {
            IconButton(
                enabled = !enabled,
                onClick = { scope.launch { drawerState.open() } },
                modifier = Modifier
                    .size(ICON_CLICK_DIM)
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.bars),
                    contentDescription = "Menu section Icon",
                    modifier = Modifier.size(ICON_SIZE)
                )
            }
        },
        title = {
            Text(
                "ValheimViki",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineLarge,
            )
        },
        actions = {
            IconButton(
                onClick = { onBookMarkClick() },
                modifier = Modifier
                    .size(ICON_CLICK_DIM)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bookmarks),
                    contentDescription = "Bookmarks section Icon",
                    modifier = Modifier.size(ICON_SIZE)
                )
            }
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(ICON_CLICK_DIM)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_search),
                    contentDescription = "Search section Icon",
                    modifier = Modifier.size(ICON_SIZE)
                )
            }
        }

    )
}

@Preview(
    name = "HomeTopBar",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewHomeTopBar() {
    ValheimVikiAppTheme { // Ensure your app theme is applied
        MainAppBar(
            onSearchBarClick = {},
            onMenuClick = {},
            onBookMarkClick = {},
            scope = rememberCoroutineScope(),
            drawerState = rememberDrawerState(DrawerValue.Closed)
        )
    }
}