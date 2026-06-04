package com.rabbitv.valheimviki.presentation.components.section_header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rabbitv.valheimviki.R


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
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		upper?.let {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					data.icon,
					contentDescription = stringResource(R.string.cd_rectangle_section_icon)
				)
				Spacer(modifier = Modifier.width(11.dp))
				Text(
					text = it,
					modifier = Modifier.widthIn(max = 520.dp),
					style = MaterialTheme.typography.headlineSmall,
					autoSize = TextAutoSize.StepBased(
						minFontSize = 16.sp,
						maxFontSize = 24.sp,
						stepSize = 1.sp,
					),
					textAlign = TextAlign.Center,
					maxLines = 2,
				)
			}
		}
		data.subTitle?.let {
			Spacer(modifier = Modifier.padding(6.dp))
			Text(
				text = it,
				modifier = Modifier.fillMaxWidth(),
				style = MaterialTheme.typography.labelLarge,
				textAlign = TextAlign.Center
			)
		}
	}
}
