package com.rabbitv.valheimviki.presentation.components.button


import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Gavel
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DarkGlassButton(
	modifier: Modifier = Modifier,
	onCardClick: () -> Unit,
	icon: ImageVector? = null,
	label: String? = null,
	value: String? = null,
	height: Dp = 60.dp
) {
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
				.padding(horizontal = 24.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			if (icon != null) {
				Icon(
					imageVector = icon,
					contentDescription = null,
					tint = Color(0xFFFF6B35),
					modifier = Modifier.size(18.dp)
				)
			}

			Spacer(modifier = Modifier.width(16.dp))

			if (label != null) {
				Text(
					text = label,
					color = Color(0xFFFF6B35),
					style = MaterialTheme.typography.bodyLarge.copy(
						fontSize = 16.sp,
						fontWeight = FontWeight.Bold
					),
					letterSpacing = 1.sp
				)
			}

			Spacer(modifier = Modifier.width(10.dp))

			if (value != null) {
				Text(
					modifier = Modifier
						.weight(1f)
						.padding(horizontal = 5.dp),
					text = value,
					color = Color.White,
					style = MaterialTheme.typography.bodyLarge.copy(
						fontWeight = FontWeight.Bold,
						lineHeight = 18.sp
					),
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					textAlign = TextAlign.End
				)
			}
			IconButton(
				onClick = { onCardClick() },
			) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
					contentDescription = null,
					tint = Color(0xFFFF6B35),
					modifier = Modifier.size(24.dp)
				)
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
			label = "Gavel",
			value = null,
			icon = Lucide.Gavel
		)
	}
}
