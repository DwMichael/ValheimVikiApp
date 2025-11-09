package com.rabbitv.valheimviki.presentation.settings

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.composables.icons.lucide.Book
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Coffee
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.presentation.components.button.DarkGlassButton
import com.rabbitv.valheimviki.presentation.components.dialog_pop_up.DialogPopUp
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.settings.viewmodel.SettingsViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.Constants.VALHEIM_VIKI_LINK

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SettingsScreen(
	modifier: Modifier = Modifier,
	onBack: () -> Unit,
	onItemClick: (DetailDestination) -> Unit,
	viewModel: SettingsViewModel = hiltViewModel()
) {
	SettingsScreenContent(modifier, onBack)
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SettingsScreenContent(
	modifier: Modifier,
	onBack: () -> Unit,
) {
	val context = LocalContext.current
	val showDialog = remember { mutableStateOf(false) }

	Scaffold(
		modifier = modifier.testTag("SettingsListScaffold"),
		topBar = {
			SimpleTopBar(
				title = "Settings",
				onClick = {
					onBack()
				}
			)
		},
	)
	{ innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
				.padding(BODY_CONTENT_PADDING.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top,

			) {
			Spacer(modifier = Modifier.height(BODY_CONTENT_PADDING.dp))
			DarkGlassCard(onCardClick = {})
			Spacer(modifier = Modifier.height(20.dp))
			HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
			Spacer(modifier = Modifier.height(20.dp))
			DarkGlassButton(
				onCardClick = {
					val intent = Intent(Intent.ACTION_VIEW, VALHEIM_VIKI_LINK.toUri())
					context.startActivity(intent)
				},
				icon = Lucide.Book,
				label = stringResource(R.string.valheim_wiki_fandom),
				value = null
			)
			Spacer(modifier = Modifier.height(20.dp))
			DarkGlassButton(
				onCardClick = {
					showDialog.value = true
				},
				icon = Lucide.Coffee,
				label = stringResource(R.string.button_donate_pop_up_label),
				value = null

			)
		}
	}
	if (showDialog.value) {
		Dialog(
			onDismissRequest = { showDialog.value = false },
			properties = DialogProperties(
				usePlatformDefaultWidth = false
			)
		) {
			DialogPopUp(
				onDismiss = { showDialog.value = false },
				text = stringResource(R.string.dialog_donate_pop_up_label),
				icon = Lucide.CircleAlert
			)
		}
	}
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DarkGlassCard(
	modifier: Modifier = Modifier,
	onCardClick: () -> Unit,
	icon: ImageVector = Lucide.CircleAlert,
	text: String = stringResource(R.string.valheim_viki_fandom_infromation_settings),
) {
	Box(
		modifier = modifier
			.fillMaxWidth(0.8f)
			.wrapContentHeight()
			.clip(Shapes.large)
			.background(Color.Black.copy(alpha = 0.3f))
			.border(
				width = 1.dp,
				color = Color(0xFF4A4A4A).copy(alpha = 0.5f),
				shape = Shapes.large
			)
			.clickable { onCardClick() }
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
			modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp),
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
				modifier = Modifier
					.padding(top = 8.dp),
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
			text = "This app uses some images and game data from the Valheim Wiki All rights to Valheim belong to Iron Gate Studio and Coffee Stain Publishing. For more details and complete game information, please visit the Valheim Wiki",
			onCardClick = {}
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