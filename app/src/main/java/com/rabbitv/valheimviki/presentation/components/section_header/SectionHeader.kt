package com.rabbitv.valheimviki.presentation.components.section_header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Immutable
data class SectionHeaderData(
	val title: String?,
	val subTitle: String?,
	val icon: ImageVector,
)

@Composable
fun SectionHeader(
	data: SectionHeaderData,
	modifier: Modifier = Modifier,
) {
	val upper = remember(data.title) { data.title?.uppercase() }
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.Start
	) {
		upper?.let {
			Row {
				Icon(data.icon, contentDescription = "Rectangle section Icon")
				Spacer(modifier = Modifier.width(11.dp))
				Text(it, style = MaterialTheme.typography.titleLarge)
			}
		}
		data.subTitle?.let {
			Spacer(modifier = Modifier.padding(6.dp))
			Text(it, style = MaterialTheme.typography.titleMedium)
		}
	}
}
