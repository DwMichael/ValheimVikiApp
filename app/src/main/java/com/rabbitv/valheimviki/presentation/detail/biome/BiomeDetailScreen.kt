package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.ContentAlpha
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.ui.theme.MEDIUM_PADDING


@Composable
fun BiomeDetailScreen(

    viewModel: BiomeScreenViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {

    val biome: Biome by viewModel.

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(232.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = MEDIUM_PADDING,
                        bottomEnd = MEDIUM_PADDING
                    )
                ),
            tonalElevation = 0.dp,
            color = Color.Black.copy(alpha = ContentAlpha.medium),
        ) {
            Text(
                text = biome.name,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding
                        (horizontal = 8.dp),
            )
        }
    }


}

@Preview(
    name = "BiomeDetail",
    showBackground = true
)
@Composable
private fun PreviewBiomeDetail() {
    BiomeDetailScreen(paddingValues = PaddingValues(0.dp))
}