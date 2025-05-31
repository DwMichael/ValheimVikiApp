package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CircleHelp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ServerOff
import com.composables.icons.lucide.WifiOff
import com.rabbitv.valheimviki.domain.model.ui_state.default_list_state.ErrorType
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun EmptyScreen(
    errorMessage: String,
    errorType: ErrorType = ErrorType.INTERNET_CONNECTION

) {
    val selectedImageVector = when (errorType) {
        ErrorType.SERVER_DOWN -> Lucide.ServerOff
        ErrorType.UNKNOWN_ERROR -> Lucide.CircleHelp
        ErrorType.INTERNET_CONNECTION -> Lucide.WifiOff
    }
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
                    imageVector = selectedImageVector,
                    tint = Color.White,
                    contentDescription = "NO_INTERNET_CONECTION_ICON",
                )
            Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
            Text(
                errorMessage,
                textAlign = TextAlign.Center
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "EmptyScreen")
@Composable
private fun PreviewEmptyScreen() {
    ValheimVikiAppTheme {
        EmptyScreen(errorMessage = "Connection False", errorType = ErrorType.SERVER_DOWN)
    }
}