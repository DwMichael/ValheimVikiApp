package com.rabbitv.valheimviki.presentation.components.grid.custom_column_grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.repository.Droppable
import com.rabbitv.valheimviki.presentation.components.grid.grid_item.RecipeItemCard

import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@Composable
fun CustomColumnGrid(recipeItems: List<Droppable>) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = BODY_CONTENT_PADDING.dp),
		verticalArrangement = Arrangement.spacedBy(BODY_CONTENT_PADDING.dp)
	) {
		recipeItems.chunked(2).forEach { rowItems ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(
					BODY_CONTENT_PADDING.dp
				)
			) {
				rowItems.forEach { item ->
					Box(
						modifier = Modifier.weight(1f)
					) {
						RecipeItemCard(item = item)
					}
				}
				if (rowItems.size == 1) {
					Spacer(modifier = Modifier.weight(1f))
				}
			}
		}
	}
}