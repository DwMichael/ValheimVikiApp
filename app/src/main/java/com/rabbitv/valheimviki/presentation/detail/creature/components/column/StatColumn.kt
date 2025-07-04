package com.rabbitv.valheimviki.presentation.detail.creature.components.column

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@Composable
fun StatColumn(stat: String) {
	val statList: List<String> =
		stat.split(", ")
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = BODY_CONTENT_PADDING.dp * 2, end = BODY_CONTENT_PADDING.dp),
	) {
		statList.forEach { item ->
			Text(
				text = item,

				style = MaterialTheme.typography.bodyLarge
			)
		}
	}
}