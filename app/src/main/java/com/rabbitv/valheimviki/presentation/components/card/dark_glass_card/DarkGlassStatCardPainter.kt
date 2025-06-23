package com.rabbitv.valheimviki.presentation.components.card.dark_glass_card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.presentation.detail.food.BaseDarkGlassStatCard

@Composable
fun DarkGlassStatCardPainter(
	painter: Painter,
	label: String,
	value: String,
	modifier: Modifier = Modifier,
	expand: () -> Unit,
	isExpanded: Boolean,
) {
	BaseDarkGlassStatCard(
		iconContent = {
			Icon(
				painter = painter,
				contentDescription = null,
				tint = Color(0xFFFF6B35),
				modifier = Modifier.size(24.dp)
			)
		},
		label = label,
		value = value,
		modifier = modifier.clickable { expand() },
		expand = expand,
		isExpanded = isExpanded
	)
}