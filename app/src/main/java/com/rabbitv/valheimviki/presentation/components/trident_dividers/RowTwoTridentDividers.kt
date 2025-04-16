package com.rabbitv.valheimviki.presentation.components.trident_dividers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.presentation.detail.biome.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun RowTwoTridentDividers() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BODY_CONTENT_PADDING.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TridentDivider(
            modifier = Modifier
                .weight(1f)
                .rotate(180f)
        )
        Spacer(Modifier.weight(1f))
        TridentDivider(modifier = Modifier.weight(1f))
    }
}

@Preview("RowTwoTridentDividers", showBackground = true)
@Composable
fun PreviewRowTwoTridentDividers() {
    ValheimVikiAppTheme {
        RowTwoTridentDividers()
    }
}