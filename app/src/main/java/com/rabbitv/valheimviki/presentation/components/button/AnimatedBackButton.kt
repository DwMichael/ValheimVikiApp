package com.rabbitv.valheimviki.presentation.components.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark

@Composable
fun AnimatedBackButton(
	modifier: Modifier = Modifier,
	scrollState: ScrollState,
	onBack: () -> Unit,
) {
	val previousScrollValue = remember { mutableIntStateOf(0) }
	val backButtonVisibleState by remember {
		derivedStateOf {
			val currentScroll = scrollState.value
			val previous = previousScrollValue.intValue
			val isVisible = when {
				currentScroll == 0 -> true
				currentScroll < previous -> true
				currentScroll > previous -> false
				else -> true
			}
			previousScrollValue.intValue = currentScroll
			isVisible
		}
	}
	AnimatedVisibility(
		visible = backButtonVisibleState,
		enter = fadeIn(),
		exit = fadeOut(),
		modifier = modifier
	) {
		FilledIconButton(
			onClick = onBack,
			shape = RoundedCornerShape(12.dp),
			colors = IconButtonDefaults.filledIconButtonColors(
				containerColor = ForestGreen10Dark,
			),
			modifier = Modifier.size(56.dp)
		) {
			Icon(
				Icons.AutoMirrored.Rounded.ArrowBack,
				contentDescription = "Back",
				tint = Color.White
			)
		}
	}
}