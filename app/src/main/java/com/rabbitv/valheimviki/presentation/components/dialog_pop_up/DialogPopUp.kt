package com.rabbitv.valheimviki.presentation.components.dialog_pop_up

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Coffee
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.components.button.DarkGlassButton
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.Constants.TIP_LINK

@Composable
fun DialogPopUp(
	modifier: Modifier = Modifier,
	onDismiss: () -> Unit,
	icon: ImageVector = Lucide.CircleAlert,
	text: String = stringResource(R.string.valheim_viki_fandom_infromation_settings),
) {
	val context = LocalContext.current

	Box(
		modifier = modifier
			.fillMaxWidth(0.90f)
			.wrapContentHeight()
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
			Spacer(modifier = Modifier.height(20.dp))
			DarkGlassButton(
				onCardClick = {
					onDismiss()
				},
				icon = Lucide.X,
				label = "Go Back",
				value = null,
				leadIcon = false
			)

			Spacer(modifier = Modifier.height(20.dp))
			DarkGlassButton(
				onCardClick = {
					val intent = Intent(Intent.ACTION_VIEW, TIP_LINK.toUri())
					context.startActivity(intent)
					onDismiss()
				},
				icon = Lucide.Coffee,
				label = stringResource(R.string.button_donate_pop_up_label),
				value = null
			)
		}
	}
}


@Preview(name = "DialogPopUp")
@Composable
private fun PreviewDialogPopUp() {
	ValheimVikiAppTheme {
		DialogPopUp(
			modifier = Modifier,
			icon = Lucide.CircleAlert,
			text = stringResource(R.string.dialog_donate_pop_up_label),
			onDismiss = {},
		)
	}
}