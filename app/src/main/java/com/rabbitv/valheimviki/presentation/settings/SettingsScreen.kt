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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.icons.lucide.Book
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.presentation.components.button.DarkGlassButton
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.settings.viewmodel.SettingsViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.Shapes

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
					val feedbackFormUrl = "https://valheim.fandom.com/wiki/Valheim_Wiki"
					val intent = Intent(Intent.ACTION_VIEW, feedbackFormUrl.toUri())
					context.startActivity(intent)
				},
				icon = Lucide.Book,
				label = "Valheim Wiki | Fandom",
				value = null
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
	text: String = "This app is Fully Fan-made, it  uses some images and game data from the Valheim Wiki.\n\n Most of the texts and images in this app belong to the Valheim Wiki (Fandom).\n\n For more details and complete game information, please visit the Valheim Wiki",
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
		Box(
			modifier = Modifier
				.matchParentSize()
				.graphicsLayer {
					renderEffect = RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.CLAMP)
						.asComposeRenderEffect()
				}
		)

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
	DarkGlassCard(
		modifier = Modifier,
		icon = Lucide.CircleAlert,
		text = "This app uses some images and game data from the Valheim Wiki All rights to Valheim belong to Iron Gate Studio and Coffee Stain Publishing. For more details and complete game information, please visit the Valheim Wiki",
		onCardClick = {}
	)
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(name = "SettingsScreen")
@Composable
private fun PreviewSettingsScreen() {
	SettingsScreenContent(
		modifier = Modifier,
		onBack = {}

	)
}