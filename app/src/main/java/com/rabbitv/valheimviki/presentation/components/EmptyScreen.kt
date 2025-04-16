package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.WifiOff
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyScreen(
    modifier: Modifier,
    state: PullToRefreshState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    errorMessage: String,
) {
//    PullToRefreshBox(
//        state = state,
//        isRefreshing = isRefreshing,
//        onRefresh = onRefresh,
//    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .absolutePadding(
                    left = 16.dp,
                    top = 0.dp,
                    right = 16.dp,
                    bottom = 16.dp,

                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            items(1) {
                Icon(
                    modifier = Modifier.size(75.dp),
                    imageVector = Lucide.WifiOff,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.no_internet_connection),
                )
                Text(
                    errorMessage,
                    textAlign = TextAlign.Center
                )
            }

        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "EmptyScreen")
@Composable
private fun PreviewEmptyScreen() {
    ValheimVikiAppTheme {
        EmptyScreen(
            modifier = Modifier,
            state = rememberPullToRefreshState(),
            isRefreshing = false,
            onRefresh = {},
            errorMessage = "Connection False"
        )
    }
}