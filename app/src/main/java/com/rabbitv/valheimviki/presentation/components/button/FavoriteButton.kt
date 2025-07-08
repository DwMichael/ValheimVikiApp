package com.rabbitv.valheimviki.presentation.components.button

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.BookmarkCheck
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.ui.theme.MoreIntensiveOrange
import com.rabbitv.valheimviki.ui.theme.SMALL_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun FavoriteButton(
	modifier: Modifier = Modifier,
	isFavorite: Boolean,
	onToggleFavorite: () -> Unit,
) {
	FilledIconToggleButton(
		onCheckedChange = { onToggleFavorite() },
		checked = isFavorite,
		modifier = modifier
			.size(42.dp),
		colors = IconButtonDefaults.filledIconToggleButtonColors(
			containerColor = Color.Transparent,
			checkedContainerColor = Color.Transparent,

			),
		shape = RoundedCornerShape(SMALL_PADDING)
	) {
		Icon(
			modifier = Modifier.fillMaxSize(),
			imageVector = if (isFavorite) Lucide.BookmarkCheck else Lucide.Bookmark,
			contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
			tint = if (isFavorite) MoreIntensiveOrange else Color.Gray
		)
	}
}


@Preview("FavoriteButtonUnSelected")
@Composable
fun PreviewFavoriteButtonUnSelected() {
	ValheimVikiAppTheme {
		FavoriteButton(
			isFavorite = false,
			onToggleFavorite = {}
		)
	}
}

@Preview("FavoriteButtonSelected")
@Composable
fun PreviewFavoriteButtonSelected() {
	ValheimVikiAppTheme {
		FavoriteButton(
			isFavorite = true,
			onToggleFavorite = {}
		)
	}
}
