package com.rabbitv.valheimviki.presentation.components.flow_row.flow_as_grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@Composable
fun TwoColumnGrid(
	modifier: Modifier = Modifier,
	paddingValues: PaddingValues = PaddingValues(BODY_CONTENT_PADDING.dp),
	content: @Composable FlowRowScope.() -> Unit
) {
	FlowRow(
		modifier = modifier
			.wrapContentHeight()
			.fillMaxWidth()
			.padding(paddingValues),
		maxItemsInEachRow = 2,
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp),
		content = content
	)
}