package com.rabbitv.valheimviki.presentation.settings

import android.app.Activity
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Book
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Coffee
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Sparkles
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.language.AppLanguage
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.presentation.components.button.DarkGlassButton
import com.rabbitv.valheimviki.presentation.components.dialog_pop_up.DialogPopUp
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.settings.model.SettingsUiEvent
import com.rabbitv.valheimviki.presentation.settings.model.SettingsUiState
import com.rabbitv.valheimviki.presentation.settings.viewmodel.SettingsViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen20Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDTBorder
import com.rabbitv.valheimviki.utils.Constants.VALHEIM_VIKI_LINK

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SettingsScreen(
	modifier: Modifier = Modifier,
	onBack: () -> Unit,
	onItemClick: (DetailDestination) -> Unit,
	viewModel: SettingsViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val activity = LocalActivity.current as Activity
	val onEvent = remember<(SettingsUiEvent) -> Unit>(viewModel) { { viewModel.onEvent(it) } }

	// Trigger ad when ViewModel flags it
	if (uiState.showAdTrigger) {
		viewModel.showAdIfNeeded(activity)
	}

	SettingsScreenContent(
		modifier = modifier,
		onBack = onBack,
		uiState = uiState,
		onEvent = onEvent,
	)
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SettingsScreenContent(
	modifier: Modifier,
	onBack: () -> Unit,
	uiState: SettingsUiState = SettingsUiState(),
	onEvent: (SettingsUiEvent) -> Unit = {},
) {
	val context = LocalContext.current
	val showDonateDialog = remember { mutableStateOf(false) }
	val columnScrollable = rememberScrollState()

	Scaffold(
		modifier = modifier.testTag("SettingsListScaffold"),
		topBar = {
			SimpleTopBar(
				title = stringResource(R.string.settings),
				onClick = { onBack() }
			)
		},
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
				.padding(BODY_CONTENT_PADDING.dp)
				.verticalScroll(columnScrollable),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top,
		) {
			Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
			DarkGlassCard()
			HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

			Spacer(modifier = Modifier.height(10.dp))
			if (uiState.isRefetching) {
				LoadingIndicator(paddingValues = PaddingValues(16.dp))
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					text = stringResource(
						R.string.settings_downloading_data,
						uiState.currentLanguage.displayName
					),
					color = PrimaryWhite.copy(alpha = 0.7f),
					style = MaterialTheme.typography.bodyMedium,
				)
			} else {
				DarkGlassButton(
					onCardClick = { onEvent(SettingsUiEvent.ShowLanguageDialog) },
					icon = Lucide.Globe,
					label = "${uiState.currentLanguage.flagEmoji}  ${uiState.currentLanguage.displayName}",
					leadingIcon = Lucide.ChevronRight
				)
			}

			// --- Links Section ---
			Spacer(modifier = Modifier.height(20.dp))
			DarkGlassButton(
				onCardClick = {
					val intent = Intent(Intent.ACTION_VIEW, VALHEIM_VIKI_LINK.toUri())
					context.startActivity(intent)
				},
				icon = Lucide.Book,
				label = stringResource(R.string.valheim_wiki_fandom),
				leadingIcon = Lucide.ChevronRight
			)
			Spacer(modifier = Modifier.height(20.dp))
			DarkGlassButton(
				onCardClick = { showDonateDialog.value = true },
				icon = Lucide.Coffee,
				label = stringResource(R.string.button_donate_pop_up_label),
				leadingIcon = Lucide.ChevronRight
			)
		}
	}

	// Donate dialog
	if (showDonateDialog.value) {
		Dialog(
			onDismissRequest = { showDonateDialog.value = false },
			properties = DialogProperties(usePlatformDefaultWidth = false)
		) {
			DialogPopUp(
				onDismiss = { showDonateDialog.value = false },
				text = stringResource(R.string.dialog_donate_pop_up_label),
				icon = Lucide.CircleAlert
			)
		}
	}

	// Language picker dialog
	if (uiState.showLanguageDialog) {
		LanguagePickerDialog(
			currentLanguage = uiState.currentLanguage,
			onLanguageSelected = { onEvent(SettingsUiEvent.LanguageSelected(it)) },
			onDismiss = { onEvent(SettingsUiEvent.DismissLanguageDialog) }
		)
	}

	if (uiState.isLanguageSwitching) {
		LaunchedEffect(Unit) {
			kotlinx.coroutines.delay(720)
			onEvent(SettingsUiEvent.LanguageSwitchOverlayShown)
		}
		LanguageSwitchOverlay(currentLanguage = uiState.currentLanguage)
	}
}

@Composable
fun LanguagePickerDialog(
	currentLanguage: AppLanguage,
	onLanguageSelected: (AppLanguage) -> Unit,
	onDismiss: () -> Unit,
) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth(0.85f)
				.clip(RoundedCornerShape(16.dp))
				.background(ForestGreen20Dark)
				.border(
					width = 1.dp,
					color = YellowDTBorder,
					shape = RoundedCornerShape(16.dp)
				)
				.padding(24.dp)
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = stringResource(R.string.settings_select_language),
					color = PrimaryWhite,
					style = MaterialTheme.typography.headlineSmall.copy(
						fontWeight = FontWeight.Bold
					)
				)
				Spacer(Modifier.height(20.dp))

				AppLanguage.entries.forEach { language ->
					LanguageOptionRow(
						language = language,
						isSelected = language == currentLanguage,
						onClick = { onLanguageSelected(language) }
					)
					if (language != AppLanguage.entries.last()) {
						Spacer(Modifier.height(8.dp))
					}
				}
			}
		}
	}
}

@Composable
private fun LanguageOptionRow(
	language: AppLanguage,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	val cardShape = RoundedCornerShape(16.dp)
	val backgroundBrush = if (isSelected) {
		Brush.horizontalGradient(
			colors = listOf(
				Color(0xFF244A4F),
				Color(0xFF1A373B),
			)
		)
	} else {
		Brush.horizontalGradient(
			colors = listOf(
				Color(0xFF10191B),
				Color(0xFF0C1416),
			)
		)
	}

	val borderBrush = if (isSelected) {
		Brush.linearGradient(listOf(Color(0xFFE3D2AE), Color(0xFFB49D73)))
	} else {
		Brush.linearGradient(listOf(Color.White.copy(alpha = 0.20f), Color.White.copy(alpha = 0.08f)))
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clip(cardShape)
			.background(backgroundBrush)
			.border(width = 1.dp, brush = borderBrush, shape = cardShape)
			.clickable { onClick() }
			.padding(horizontal = 14.dp, vertical = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = Modifier
				.size(46.dp)
				.clip(RoundedCornerShape(13.dp))
				.background(Color.White.copy(alpha = if (isSelected) 0.14f else 0.08f))
				.border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(13.dp)),
			contentAlignment = Alignment.Center,
		) {
			Text(
				text = language.flagEmoji,
				style = MaterialTheme.typography.headlineSmall,
			)
		}

		Spacer(Modifier.width(12.dp))

		Column(modifier = Modifier.weight(1f)) {
			Text(
				text = language.nativeLabel,
				color = if (isSelected) Color(0xFFF3E6CA) else PrimaryWhite,
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
			)
			Text(
				text = language.displayName.uppercase(),
				color = if (isSelected) Color(0xFFD5C3A2) else PrimaryWhite.copy(alpha = 0.68f),
				style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
			)
		}

		Box(
			modifier = Modifier
				.size(28.dp)
				.clip(CircleShape)
				.background(if (isSelected) Color(0xFFE7D6B3) else Color.Transparent)
				.border(
					width = 1.dp,
					color = if (isSelected) Color(0xFFE7D6B3) else PrimaryWhite.copy(alpha = 0.3f),
					shape = CircleShape,
				),
			contentAlignment = Alignment.Center,
		) {
			if (isSelected) {
				Icon(
					imageVector = Lucide.Check,
					contentDescription = null,
					tint = Color(0xFF1B3438),
					modifier = Modifier.size(16.dp),
				)
			}
		}
	}
}

@Composable
private fun LanguageSwitchOverlay(currentLanguage: AppLanguage) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(Color(0xCC071012)),
		contentAlignment = Alignment.Center,
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth(0.78f)
				.clip(RoundedCornerShape(22.dp))
				.background(
					Brush.verticalGradient(
						listOf(
							Color(0xFF193E43),
							Color(0xFF102B2F),
						)
					)
				)
				.border(
					1.dp,
					brush = Brush.linearGradient(listOf(Color(0xFFD9C7A4), Color(0xFF8F7B57))),
					shape = RoundedCornerShape(22.dp),
				)
				.padding(horizontal = 20.dp, vertical = 24.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Icon(
				imageVector = Lucide.Sparkles,
				contentDescription = null,
				tint = Color(0xFFE7D5B2),
				modifier = Modifier.size(24.dp),
			)
			Spacer(Modifier.height(12.dp))
			Text(
				text = stringResource(R.string.settings_applying_language),
				color = PrimaryWhite,
				style = MaterialTheme.typography.titleMedium,
				textAlign = TextAlign.Center,
			)
			Spacer(Modifier.height(6.dp))
			Text(
				text = "${currentLanguage.flagEmoji} ${currentLanguage.nativeLabel}",
				color = Color(0xFFD9C7A4),
				style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
			)
			Spacer(Modifier.height(18.dp))
			CircularProgressIndicator(
				color = Color(0xFFE7D5B2),
				strokeWidth = 2.6.dp,
			)
		}
	}
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DarkGlassCard(
	modifier: Modifier = Modifier,
	icon: ImageVector = Lucide.CircleAlert,
	text: String = stringResource(R.string.valheim_viki_fandom_infromation_settings),
) {
	Box(
		modifier = modifier
			.padding(20.dp)
			.wrapContentSize(Alignment.Center)
			.clip(Shapes.large)
			.background(Color.Black.copy(alpha = 0.3f))
			.border(
				width = 1.dp,
				color = Color(0xFF4A4A4A).copy(alpha = 0.5f),
				shape = Shapes.large
			)
	) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			Box(
				modifier = Modifier
					.matchParentSize()
					.graphicsLayer {
						renderEffect =
							RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.CLAMP)
								.asComposeRenderEffect()
					}
			)
		}
		Column(
			modifier = Modifier.padding(24.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top
		) {
			Icon(
				imageVector = icon,
				contentDescription = null,
				tint = Color(0xFFFF6B35),
				modifier = Modifier.size(24.dp)
			)
			Text(
				modifier = Modifier.padding(top = 8.dp),
				text = text,
				color = Color(0xFFFF6B35),
				style = MaterialTheme.typography.bodyLarge.copy(
					fontWeight = FontWeight.Bold,
					lineHeight = 18.sp
				),
				textAlign = TextAlign.Center
			)
		}
	}
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(name = "DarkGlassCard")
@Composable
private fun PreviewDarkGlassCard() {
	ValheimVikiAppTheme {
		DarkGlassCard(
			modifier = Modifier,
			icon = Lucide.CircleAlert,
			text = stringResource(R.string.valheim_viki_fandom_infromation_settings),
		)
	}
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(name = "SettingsScreen")
@Composable
private fun PreviewSettingsScreen() {
	ValheimVikiAppTheme {
		SettingsScreenContent(
			modifier = Modifier,
			onBack = {}
		)
	}
}

@Preview(name = "LanguagePickerDialog")
@Composable
private fun PreviewLanguagePickerDialog() {
	ValheimVikiAppTheme {
		LanguagePickerDialog(
			currentLanguage = AppLanguage.ENGLISH,
			onLanguageSelected = {},
			onDismiss = {}
		)
	}
}
