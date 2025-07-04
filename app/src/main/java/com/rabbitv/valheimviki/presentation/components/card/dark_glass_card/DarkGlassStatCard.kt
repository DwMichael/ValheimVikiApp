package com.rabbitv.valheimviki.presentation.components.card.dark_glass_card

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Utensils
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@Composable
fun DarkGlassStatCard(
	modifier: Modifier = Modifier,
	icon: ImageVector,
	label: String,
	value: String,
	expand: () -> Unit,
	isExpanded: Boolean,
	baseDarkGlassStatCardHeight: Dp = 60.dp
) {
	BaseDarkGlassStatCard(
		modifier = modifier,
		iconContent = {
			Icon(
				imageVector = icon,
				contentDescription = null,
				tint = Color(0xFFFF6B35),
				modifier = Modifier.size(24.dp)
			)
		},
		label = label,
		value = value,
		expand = expand,
		isExpanded = isExpanded,
		height =baseDarkGlassStatCardHeight,
	)
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview("FoodStatCard")
@Composable
fun PreviewFoodStatCard() {

	ValheimVikiAppTheme {
		DarkGlassStatCard(icon =  Lucide.Utensils, label =   "Health", value =  "100", expand = {}, isExpanded = false)
	}

}