package com.rabbitv.valheimviki.presentation.components.section_header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerWithHeaderData


@Composable
fun SectionHeader(
	data: HorizontalPagerWithHeaderData,
	modifier: Modifier = Modifier
) {
	Column(horizontalAlignment = Alignment.Start) {
		data.title?.let {
			Row(
				horizontalArrangement = Arrangement.Start,
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					data.icon,
					tint = Color.White,
					contentDescription = "Rectangle section Icon",
				)
				Spacer(modifier = modifier.width(11.dp))

				Text(
					it.uppercase(),
					style = MaterialTheme.typography.titleLarge,
				)
			}
		}
		data.subTitle?.let {
			Spacer(modifier = modifier.padding(6.dp))
			Text(
				it,
				style = MaterialTheme.typography.titleMedium,
			)
		}
	}
}