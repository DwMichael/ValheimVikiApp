package com.rabbitv.valheimviki.presentation.components.button


import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Gavel
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DarkGlassButton(
	modifier: Modifier = Modifier,
	onCardClick: () -> Unit,
	icon: ImageVector? = null,
	label: String? = null,
	leadingIcon: ImageVector? = null,
	height: Dp = 60.dp
) {
	val iconSize = 18.dp
	val leadingIconSize = 24.dp

	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(height)
			.clip(Shapes.large)
			.background(
				Color.Black.copy(alpha = 0.3f)
			)
			.border(
				width = 1.dp,
				color = Color(0xFF4A4A4A).copy(alpha = 0.5f),
				shape = Shapes.large
			)
			.clickable {
				onCardClick()
			}

	) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			Box(
				modifier = Modifier
					.matchParentSize()
					.graphicsLayer {
						renderEffect = RenderEffect.createBlurEffect(
							10f,
							10f,
							Shader.TileMode.CLAMP
						).asComposeRenderEffect()
					}
			)
		}

		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier.size(leadingIconSize),
				contentAlignment = Alignment.Center
			) {
				if (icon != null) {
					Icon(
						imageVector = icon,
						contentDescription = null,
						tint = Color(0xFFFF6B35),
						modifier = Modifier.size(iconSize)
					)
				}
			}

			Box(
				modifier = Modifier.weight(1f),
				contentAlignment = Alignment.Center
			) {
				if (label != null) {
					BasicText(
						text = label,
						style = MaterialTheme.typography.bodyLarge.copy(
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							letterSpacing = 1.sp
						),
						autoSize = TextAutoSize.StepBased(
							maxFontSize = 16.sp,
							minFontSize = 12.sp,
							stepSize = 1.sp
						),
						color = { Color(0xFFFF6B35) },
						maxLines = 1
					)
				}
			}

			Box(
				modifier = Modifier.size(leadingIconSize),
				contentAlignment = Alignment.Center
			) {
				if (leadingIcon != null) {
					Icon(
						imageVector = leadingIcon,
						contentDescription = null,
						tint = Color(0xFFFF6B35),
						modifier = Modifier.size(leadingIconSize)
					)
				}
			}
		}
	}
}


@Preview("DarkGlassCardPreview")
@Composable
fun PreviewDarkGlassCard() {
	ValheimVikiAppTheme {
		DarkGlassButton(
			modifier = Modifier,
			onCardClick = {},
			label = stringResource(R.string.button_donate_pop_up_label),
			icon = Lucide.Gavel,
			leadingIcon = Lucide.ChevronRight,
		)
	}
}

@Preview("DarkGlassCardPreview No Left Icon")
@Composable
fun PreviewDarkGlassCardNoLeftIcon() {
	ValheimVikiAppTheme {
		DarkGlassButton(
			modifier = Modifier,
			onCardClick = {},
			label = "CENTERED TEXT",
			icon = null, // No left icon
			leadingIcon = Lucide.ChevronRight,
		)
	}
}

@Preview("DarkGlassCardPreview No Right Icon")
@Composable
fun PreviewDarkGlassCardNoRightIcon() {
	ValheimVikiAppTheme {
		DarkGlassButton(
			modifier = Modifier,
			onCardClick = {},
			label = "CENTERED TEXT",
			icon = Lucide.Gavel,
			leadingIcon = null, // No right icon
		)
	}
}

@Preview("DarkGlassCardPreview No Icons")
@Composable
fun PreviewDarkGlassCardNoIcons() {
	ValheimVikiAppTheme {
		DarkGlassButton(
			modifier = Modifier,
			onCardClick = {},
			label = "FULLY CENTERED TEXT",
			icon = null,
			leadingIcon = null,
		)
	}
}