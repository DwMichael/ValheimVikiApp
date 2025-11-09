package com.rabbitv.valheimviki.presentation.components.floating_action_button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronUp
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun CustomFloatingActionButton(
	modifier: Modifier = Modifier,
	showBackButton: Boolean,
	onClick: () -> Unit,
	bottomPadding: Dp = 45.dp
) {
	AnimatedVisibility(
		visible = showBackButton,
		enter = fadeIn(animationSpec = tween(300)),
		exit = fadeOut(animationSpec = tween(300)),
		modifier = modifier.padding(bottom = bottomPadding, top = 45.dp, end = 10.dp, start = 10.dp)
	) {
		FloatingActionButton(
			onClick = onClick,
			shape = RoundedCornerShape(BODY_CONTENT_PADDING.dp),
			containerColor = ForestGreen10Dark,
			contentColor = PrimaryWhite,
			elevation = FloatingActionButtonDefaults.elevation(),
		) {
			Icon(
				Lucide.ChevronUp,
				contentDescription = "Button Up",
				modifier = Modifier.size(ICON_SIZE)
			)
		}
	}
}


@Preview("CustomFloatingActionButton", widthDp = 80, heightDp = 80)
@Composable
fun PreviewCustomFloatingActionButton() {
	val backButtonVisibleState = true

	val backToTopState = remember { mutableStateOf(false) }
	ValheimVikiAppTheme {
		CustomFloatingActionButton(
			showBackButton = backButtonVisibleState,
			onClick = { backToTopState.value = true },

			)
	}
}