package com.rabbitv.valheimviki.presentation.home


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rabbitv.valheimviki.navigation.ChildNavGraph
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeContent(
    drawerState: DrawerState,
    scope: CoroutineScope,
    childNavController: NavHostController
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                onSearchBarClick = {},
                onMenuClick = { scope.launch { drawerState.open() } },
                onBookMarkClick = {},
                scope = scope,
                drawerState = drawerState
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                ChildNavGraph(
                    paddingValues = innerPadding,
                    navHostController = childNavController
                )
            }
        }
    )
}

@Preview(name = "HomeContent")
@Composable
private fun PreviewHomeContent() {
    ValheimVikiAppTheme {
        HomeContent(
            drawerState = DrawerState(DrawerValue.Closed),
            scope = rememberCoroutineScope(),
            childNavController = rememberNavController()
        )
    }
}