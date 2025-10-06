package com.rabbitv.valheimviki.presentation.components.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark

@Composable
fun SimpleTopBar(
    modifier: Modifier = Modifier,
    title:String?,
    onClick: () -> Unit
) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 10.dp)
        ) {
            FilledIconButton(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = ForestGreen10Dark,
                ),
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            if (title != null) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }
}

@Preview(name = "SimpleTopBarPreview")
@Composable
private fun PreviewSimpleToBar() {
    SimpleTopBar(
        modifier = Modifier,
        title = "Top App Bar",
        onClick = {}
    )
}
