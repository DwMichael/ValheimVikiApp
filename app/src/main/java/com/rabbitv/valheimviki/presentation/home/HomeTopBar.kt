package com.rabbitv.valheimviki.presentation.home

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onSearchBarClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onBookMarkClick: () -> Unit = {},
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    painter = painterResource(id = R.drawable.bars),
                    contentDescription = "Menu section",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        title = {
            Text(
                "ValheimViki",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        actions = {
            FilledIconButton(
                onClick = { /*TODO*/ },
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    tint = Color.Black,
                    painter = painterResource(R.drawable.loop),
                    contentDescription = "Search section",
                    modifier = Modifier.size(24.dp)
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
    ValheimVikiAppTheme  { // Ensure your app theme is applied
        HomeTopBar(
            onSearchBarClick = {},
            onMenuClick = {},
            onBookMarkClick = {},
            scope = rememberCoroutineScope(),
            drawerState = rememberDrawerState(DrawerValue.Closed)
        )
    }
}