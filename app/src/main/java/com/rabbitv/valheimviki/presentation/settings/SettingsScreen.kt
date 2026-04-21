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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.layout.heightIn
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
			kotlinx.coroutines.delay(920)
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
				.fillMaxWidth(0.9f)
				.clip(RoundedCornerShape(20.dp))
				.background(Color(0xFF0B1116))
				.border(
					width = 1.dp,
					color = Color(0xFF202A33),
					shape = RoundedCornerShape(20.dp)
				)
				.padding(20.dp)
		) {
			Column(horizontalAlignment = Alignment.CenterHorizontally) {
				Text(
					text = stringResource(R.string.settings_select_language),
					color = Color(0xFFE8EDF3),
					style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
				)
				Spacer(Modifier.height(14.dp))

				LazyColumn(
					modifier = Modifier
						.fillMaxWidth()
						.heightIn(max = 360.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					items(AppLanguage.entries) { language ->
						LanguageOptionRow(
							language = language,
							isSelected = language == currentLanguage,
							onClick = { onLanguageSelected(language) }
						)
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
	val cardShape = RoundedCornerShape(14.dp)
	val bg = if (isSelected) Color(0xFF161F27) else Color(0xFF0E151C)
	val border = if (isSelected) Color(0xFF3A4A59) else Color(0xFF222C35)

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clip(cardShape)
			.background(bg)
			.border(1.dp, border, cardShape)
			.clickable { onClick() }
			.padding(horizontal = 14.dp, vertical = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = Modifier
				.size(42.dp)
				.clip(RoundedCornerShape(11.dp))
				.background(Color(0xFF18222B)),
			contentAlignment = Alignment.Center,
		) {
			Text(text = language.flagEmoji, style = MaterialTheme.typography.titleLarge)
		}

		Spacer(Modifier.width(12.dp))

		Column(modifier = Modifier.weight(1f)) {
			Text(
				text = language.nativeLabel,
				color = Color(0xFFE9EEF4),
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
			)
			Text(
				text = language.displayName,
				color = Color(0xFFAAB7C4),
				style = MaterialTheme.typography.bodySmall,
			)
		}

		Box(
			modifier = Modifier
				.size(22.dp)
				.clip(CircleShape)
				.background(if (isSelected) Color(0xFF7F95AA) else Color.Transparent)
				.border(
					width = 1.dp,
					color = if (isSelected) Color(0xFF7F95AA) else Color(0xFF394754),
					shape = CircleShape,
				),
			contentAlignment = Alignment.Center,
		) {
			if (isSelected) {
				Icon(
					imageVector = Lucide.Check,
					contentDescription = null,
					tint = Color(0xFF0C141B),
					modifier = Modifier.size(13.dp),
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
			.background(Color(0xCC080D12)),
		contentAlignment = Alignment.Center,
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth(0.76f)
				.clip(RoundedCornerShape(20.dp))
				.background(Color(0xFF0F161D))
				.border(1.dp, Color(0xFF26303A), RoundedCornerShape(20.dp))
				.padding(horizontal = 20.dp, vertical = 22.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = stringResource(R.string.settings_applying_language),
				color = Color(0xFFE7EDF4),
				style = MaterialTheme.typography.titleMedium,
				textAlign = TextAlign.Center,
			)
			Spacer(Modifier.height(8.dp))
			Text(
				text = "${currentLanguage.flagEmoji} ${currentLanguage.nativeLabel}",
				color = Color(0xFFA9B6C3),
				style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
			)
			Spacer(Modifier.height(16.dp))
			CircularProgressIndicator(
				color = Color(0xFF8EA1B4),
				strokeWidth = 2.4.dp,
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
