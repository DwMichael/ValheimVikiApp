package com.rabbitv.valheimviki.presentation.detail.creature.components.rows

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rabbitv.valheimviki.presentation.detail.creature.components.buttons.StarLevelButton

@Composable
fun StarLevelRow(
    onClick: (level: Int) -> Unit,
    levelsNumber: Int,
    pageIndex: Int,
    paddingValues: PaddingValues
) {

    Row(
        Modifier.padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = "Levels :",
            style = MaterialTheme.typography.titleLarge
        )
        repeat(levelsNumber) {
            if (pageIndex + 1 <= it) {
                StarLevelButton(
                    starNumber = it,
                    isFilled = false,
                    onClick = onClick,
                )
            } else {
                StarLevelButton(
                    starNumber = it,
                    isFilled = true,
                    onClick = onClick
                )
            }
        }
    }
}