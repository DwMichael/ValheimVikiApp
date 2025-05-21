package com.rabbitv.valheimviki.presentation.components.trident_divider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun TridentsDividedRow(
    text:String? = null
) {
    val padding = if(text != null) 8.dp else 0.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BODY_CONTENT_PADDING.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TridentDivider(
            modifier = Modifier
                .weight(1f, fill = text != null)
                .rotate(180f)
                .padding(start = padding)
        )
        if(text != null) {
            Text(
                modifier = Modifier
                    .weight(2.6f, fill = false),
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        } else {
            Spacer(Modifier.weight(1f))
        }

        TridentDivider(
            modifier = Modifier
                .weight(1f, fill = text != null)
                .padding(start = padding)
        )
    }
}

@Preview("RowTwoTridentDividers With Text", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewRowTwoTridentDividersWithText() {
    ValheimVikiAppTheme {
        TridentsDividedRow(
            "BOSS DETAIL"
        )
    }
}

@Preview("RowTwoTridentDividers", showBackground = true)
@Composable
fun PreviewRowTwoTridentDividers() {
    ValheimVikiAppTheme {
        TridentsDividedRow()
    }
}