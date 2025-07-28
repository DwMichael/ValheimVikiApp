package com.rabbitv.valheimviki.presentation.components.topbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
	modifier: Modifier = Modifier,
	searchQuery: String,
	updateSearchQuery: (query: String) -> Unit,
) {
	val keyboardController = LocalSoftwareKeyboardController.current
	SearchBar(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = BODY_CONTENT_PADDING.dp)
			.padding(top = 8.dp)
			.clip(Shapes.medium),
		shape = Shapes.medium,
		inputField = {
			SearchBarDefaults.InputField(
				query = searchQuery,
				onQueryChange = updateSearchQuery,
				onSearch = { keyboardController?.hide() },
				expanded = false,
				onExpandedChange = {},
				enabled = true,
				placeholder = {
					Text(
						"Describe what you looking for..",
						style = MaterialTheme.typography.labelSmall
					)
				},
				leadingIcon = {
					Icon(
						Icons.Default.Search,
						tint = PrimaryOrange,
						contentDescription = "Search Icon"
					)
				},
				trailingIcon = {
					IconButton(
						onClick = { updateSearchQuery("") },
					) {
						Icon(
							Lucide.X,
							tint = PrimaryOrange,
							contentDescription = "ArrowBack Icon",
						)
					}
				},
				colors = TextFieldDefaults.colors(
					focusedContainerColor = ForestGreen10Dark,
					unfocusedContainerColor = ForestGreen10Dark,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					cursorColor = PrimaryOrange,
					focusedLeadingIconColor = PrimaryOrange,
					unfocusedLeadingIconColor = PrimaryOrange,
				),
			)
		},
		expanded = false,
		onExpandedChange = { },
		colors = SearchBarDefaults.colors(),
		tonalElevation = SearchBarDefaults.TonalElevation,
		shadowElevation = SearchBarDefaults.ShadowElevation,
		windowInsets = WindowInsets(0.dp),
		content = {},
	)
}

@Preview(
	name = "SearchTopBar - Empty",
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewSearchTopBarEmpty() {
	ValheimVikiAppTheme {
		SearchTopBar(
			searchQuery = "",
			updateSearchQuery = {},
		)
	}
}

@Preview(
	name = "SearchTopBar - With Query",
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewSearchTopBarWithQuery() {
	ValheimVikiAppTheme {
		SearchTopBar(
			searchQuery = "Sword",
			updateSearchQuery = {},
		)
	}
}