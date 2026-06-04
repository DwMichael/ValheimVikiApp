package com.rabbitv.valheimviki.presentation.components.language_popup

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Coffee
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.components.button.DarkGlassButton
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.utils.Constants.TIP_LINK

@Composable
fun LanguageNotificationDialog(
	viewModel: LanguageNotificationViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsState()
	if (!uiState.visible) return

	val context = LocalContext.current
	Dialog(
		onDismissRequest = {},
		properties = DialogProperties(
			dismissOnBackPress = false,
			dismissOnClickOutside = false,
			usePlatformDefaultWidth = false
		)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.78f))
				.padding(horizontal = 20.dp, vertical = 28.dp),
			contentAlignment = Alignment.Center
		) {
			Surface(
				modifier = Modifier
					.fillMaxWidth()
					.widthIn(max = 720.dp)
					.clip(Shapes.large)
					.border(
						width = 1.dp,
						color = PrimaryOrange.copy(alpha = 0.45f),
						shape = Shapes.large
					),
				color = Color(0xF2050B0B),
				shadowElevation = 16.dp
			) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(20.dp)
						.verticalScroll(rememberScrollState()),
					verticalArrangement = Arrangement.spacedBy(16.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Icon(
						imageVector = Lucide.CircleAlert,
						contentDescription = null,
						tint = PrimaryOrange,
						modifier = Modifier.size(30.dp)
					)

					Text(
						text = stringResource(R.string.language_popup_title),
						color = PrimaryWhite,
						textAlign = TextAlign.Center,
						style = MaterialTheme.typography.headlineSmall.copy(
							fontWeight = FontWeight.Bold,
							letterSpacing = 1.sp
						)
					)

					Text(
						text = stringResource(R.string.language_popup_message),
						color = PrimaryWhite.copy(alpha = 0.78f),
						textAlign = TextAlign.Center,
						style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
					)

					LanguagePath()

					DarkGlassButton(
						onCardClick = {
							context.startActivity(Intent(Intent.ACTION_VIEW, TIP_LINK.toUri()))
						},
						icon = Lucide.Coffee,
						label = stringResource(R.string.button_donate_pop_up_label),
						leadingIcon = Lucide.ChevronRight,
						height = 54.dp
					)

					DarkGlassButton(
						onCardClick = viewModel::dismiss,
						enabled = uiState.canDismiss,
						icon = Lucide.CircleAlert,
						label = if (uiState.canDismiss) {
							stringResource(R.string.language_popup_dismiss)
						} else {
							stringResource(R.string.language_popup_wait, uiState.secondsRemaining)
						},
						height = 54.dp
					)
				}
			}
		}
	}
}

@Composable
private fun LanguagePath() {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
		verticalAlignment = Alignment.CenterVertically
	) {
		LanguagePathStep(
			icon = Lucide.Settings,
			label = stringResource(R.string.settings),
			modifier = Modifier.weight(1f)
		)
		Icon(
			imageVector = Lucide.ChevronRight,
			contentDescription = null,
			tint = PrimaryOrange,
			modifier = Modifier.size(22.dp)
		)
		LanguagePathStep(
			icon = Lucide.Globe,
			label = stringResource(R.string.language),
			modifier = Modifier.weight(1f)
		)
	}
}

@Composable
private fun LanguagePathStep(
	icon: ImageVector,
	label: String,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.clip(Shapes.medium)
			.background(Color.Black.copy(alpha = 0.34f))
			.border(
				width = 1.dp,
				color = Color(0xFF4A4A4A).copy(alpha = 0.6f),
				shape = Shapes.medium
			)
			.padding(horizontal = 12.dp, vertical = 12.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Icon(
			imageVector = icon,
			contentDescription = null,
			tint = PrimaryOrange,
			modifier = Modifier.size(22.dp)
		)
		Text(
			text = label,
			color = PrimaryWhite,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
			maxLines = 2
		)
	}
}
