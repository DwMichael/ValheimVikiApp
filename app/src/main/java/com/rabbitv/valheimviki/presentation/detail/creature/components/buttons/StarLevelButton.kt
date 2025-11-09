package com.rabbitv.valheimviki.presentation.detail.creature.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Star
import com.rabbitv.valheimviki.ui.theme.ICON_CLICK_DIM
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE_SECOND
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun StarLevelButton(
	onClick: (level: Int) -> Unit,
	starNumber: Int,
	isFilled: Boolean,
) {
	IconButton(
		onClick = {
			onClick(starNumber)
		},
		modifier = Modifier
			.size(ICON_CLICK_DIM)
	) {
		if (isFilled) {
			Icon(
				imageVector = Lucide.Star,
				contentDescription = "Star",
				modifier = Modifier.size(ICON_SIZE_SECOND),
				tint = Color.Yellow
			)
		} else {
			Icon(
				imageVector = Lucide.Star,
				contentDescription = "Star",
				modifier = Modifier.size(ICON_SIZE_SECOND),
				tint = Color.Gray
			)
		}
	}
}


@Preview(name = "StarLevelButton", showBackground = true)
@Composable
fun PreviewStarLevelButton() {
	ValheimVikiAppTheme {
		StarLevelButton(
			starNumber = 0,
			isFilled = false,
			onClick = {},
		)
	}
}