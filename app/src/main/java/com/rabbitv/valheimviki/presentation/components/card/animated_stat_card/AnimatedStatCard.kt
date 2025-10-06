package com.rabbitv.valheimviki.presentation.components.card.animated_stat_card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card_stat.DarkGlassStatCard
import com.rabbitv.valheimviki.presentation.components.card.dark_glass_card_stat.DarkGlassStatCardPainter
import com.rabbitv.valheimviki.presentation.detail.creature.components.column.StatColumn
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@Composable
fun AnimatedStatCard(
	modifier: Modifier = Modifier,
	id: String,
	painter: Painter? = null,
	icon: ImageVector? = null,
	label: String,
	value: String,
	details: String,
	isStatColumn: Boolean = false,
) {
	var expanded by rememberSaveable(id) { mutableStateOf(false) }
	if (icon != null) {
		DarkGlassStatCard(
			modifier = modifier,
			icon = icon,
			label = label,
			value = value,
			expand = { expanded = !expanded },
			isExpanded = expanded
		)
	}
	if (painter != null) {
		DarkGlassStatCardPainter(
			modifier = modifier,
			painter,
			label = label,
			value = value,
			expand = { expanded = !expanded },
			isExpanded = expanded
		)
	}
	if (!isStatColumn) {
		AnimatedVisibility(expanded) {
			Text(
				text = details,
				modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
				style = MaterialTheme.typography.bodyLarge
			)
		}
	} else {
		AnimatedVisibility(expanded) {
			StatColumn(details)
		}
	}


}