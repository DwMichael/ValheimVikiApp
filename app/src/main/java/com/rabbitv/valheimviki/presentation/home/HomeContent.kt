package com.rabbitv.valheimviki.presentation.home


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rabbitv.valheimviki.navigation.ChildNavGraph
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeContent(
    sharedTransitionScope :SharedTransitionScope,
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
                ChildNavGraph(
                    sharedTransitionScope = sharedTransitionScope,
                    paddingValues = innerPadding,
                    navHostController = childNavController
                )
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(name = "HomeContent")
@Composable
private fun PreviewHomeContent() {
    ValheimVikiAppTheme {
        SharedTransitionScope {
            AnimatedVisibility(true) {
                HomeContent(
                    drawerState = DrawerState(DrawerValue.Closed),
                    scope = rememberCoroutineScope(),
                    childNavController = rememberNavController(),
                    sharedTransitionScope = this@SharedTransitionScope,
                )
            }}
    }
}