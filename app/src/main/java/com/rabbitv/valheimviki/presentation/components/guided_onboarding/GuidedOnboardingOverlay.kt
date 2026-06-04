package com.rabbitv.valheimviki.presentation.components.guided_onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Coffee
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.onboarding.GuidedOnboardingStep
import com.rabbitv.valheimviki.presentation.components.button.DarkGlassButton
import com.rabbitv.valheimviki.ui.theme.PrimaryOrange
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.Shapes
import kotlin.math.roundToInt

@Composable
fun GuidedOnboardingOverlay(
	anchors: Map<GuidedOnboardingTarget, Rect>,
	isOnSettingsScreen: Boolean,
	navigateToSettings: () -> Unit,
	viewModel: GuidedOnboardingViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	if (!uiState.isActive) return

	BackHandler(enabled = true) {}

	LaunchedEffect(uiState.step, isOnSettingsScreen) {
		if (uiState.step.isSettingsStep && !isOnSettingsScreen) {
			navigateToSettings()
		}
	}

	val density = LocalDensity.current
	val targetBounds = uiState.step.target()?.let(anchors::get)
	val paddedTarget = targetBounds?.let { rect ->
		with(density) { rect.expand(8.dp.toPx()) }
	}

	Box(modifier = Modifier.fillMaxSize()) {
		GuidedScrim(targetBounds = paddedTarget)

		Box(
			modifier = Modifier
				.matchParentSize()
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = {}
				)
		)

		if (uiState.step == GuidedOnboardingStep.HOME_SETTINGS && paddedTarget != null) {
			Box(
				modifier = Modifier
					.offset {
						IntOffset(
							x = paddedTarget.left.roundToInt(),
							y = paddedTarget.top.roundToInt()
						)
					}
					.size(
						width = with(density) { paddedTarget.width.toDp() },
						height = with(density) { paddedTarget.height.toDp() }
					)
					.clip(Shapes.medium)
					.clickable {
						viewModel.onHomeSettingsTargetClicked()
						navigateToSettings()
					}
			)
		}

		when (uiState.step) {
			GuidedOnboardingStep.INFO_CARD -> GuidedInfoCard(
				secondsRemaining = uiState.secondsRemaining,
				canAccept = uiState.canAcceptInfoCard,
				onAccept = viewModel::acceptInfoCard,
				modifier = Modifier.align(Alignment.Center)
			)

			GuidedOnboardingStep.HOME_SETTINGS -> GuidedStepCard(
				step = uiState.step,
				showButton = false,
				onNext = {},
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.padding(bottom = 28.dp)
			)

			GuidedOnboardingStep.SETTINGS_LANGUAGE,
			GuidedOnboardingStep.SETTINGS_FANDOM,
			GuidedOnboardingStep.SETTINGS_DONATE -> GuidedStepCard(
				step = uiState.step,
				showButton = true,
				onNext = viewModel::nextSettingsStep,
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.padding(bottom = 28.dp)
			)

			GuidedOnboardingStep.DONE -> Unit
		}
	}
}

@Composable
private fun GuidedScrim(targetBounds: Rect?) {
	Canvas(
		modifier = Modifier
			.fillMaxSize()
			.graphicsLayer {
				compositingStrategy = CompositingStrategy.Offscreen
			}
	) {
		drawRect(Color.Black.copy(alpha = 0.82f))
		if (targetBounds != null) {
			val corner = CornerRadius(22.dp.toPx(), 22.dp.toPx())
			drawRoundRect(
				color = Color.Transparent,
				topLeft = Offset(targetBounds.left, targetBounds.top),
				size = Size(targetBounds.width, targetBounds.height),
				cornerRadius = corner,
				blendMode = BlendMode.Clear
			)
			drawRoundRect(
				color = PrimaryOrange,
				topLeft = Offset(targetBounds.left, targetBounds.top),
				size = Size(targetBounds.width, targetBounds.height),
				cornerRadius = corner,
				style = Stroke(width = 2.dp.toPx())
			)
		}
	}
}

@Composable
private fun GuidedInfoCard(
	secondsRemaining: Int,
	canAccept: Boolean,
	onAccept: () -> Unit,
	modifier: Modifier = Modifier
) {
	GuidedCard(modifier = modifier) {
		Icon(
			imageVector = Lucide.CircleAlert,
			contentDescription = null,
			tint = PrimaryOrange,
			modifier = Modifier.size(30.dp)
		)
		Text(
			text = stringResource(R.string.guided_onboarding_info_title),
			color = PrimaryWhite,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.headlineSmall.copy(
				fontWeight = FontWeight.Bold,
				letterSpacing = 1.sp
			)
		)
		Text(
			text = stringResource(R.string.guided_onboarding_info_message),
			color = PrimaryWhite.copy(alpha = 0.78f),
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
		)
		DarkGlassButton(
			onCardClick = onAccept,
			enabled = canAccept,
			icon = Lucide.CircleAlert,
			label = if (canAccept) {
				stringResource(R.string.guided_onboarding_info_accept)
			} else {
				stringResource(R.string.guided_onboarding_wait, secondsRemaining)
			},
			height = 54.dp
		)
	}
}

@Composable
private fun GuidedStepCard(
	step: GuidedOnboardingStep,
	showButton: Boolean,
	onNext: () -> Unit,
	modifier: Modifier = Modifier
) {
	GuidedCard(modifier = modifier) {
		Icon(
			imageVector = step.icon(),
			contentDescription = null,
			tint = PrimaryOrange,
			modifier = Modifier.size(28.dp)
		)
		Text(
			text = stringResource(step.titleRes()),
			color = PrimaryWhite,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
		)
		Text(
			text = stringResource(step.messageRes()),
			color = PrimaryWhite.copy(alpha = 0.78f),
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
		)
		if (showButton) {
			DarkGlassButton(
				onCardClick = onNext,
				icon = step.icon(),
				label = if (step == GuidedOnboardingStep.SETTINGS_DONATE) {
					stringResource(R.string.guided_onboarding_finish)
				} else {
					stringResource(R.string.guided_onboarding_next)
				},
				height = 54.dp
			)
		}
	}
}

@Composable
private fun GuidedCard(
	modifier: Modifier = Modifier,
	content: @Composable ColumnScope.() -> Unit
) {
	Surface(
		modifier = modifier
			.padding(horizontal = 20.dp)
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
			horizontalAlignment = Alignment.CenterHorizontally,
			content = content
		)
	}
}

private fun GuidedOnboardingStep.target(): GuidedOnboardingTarget? =
	when (this) {
		GuidedOnboardingStep.HOME_SETTINGS -> GuidedOnboardingTarget.HOME_SETTINGS
		GuidedOnboardingStep.SETTINGS_LANGUAGE -> GuidedOnboardingTarget.SETTINGS_LANGUAGE
		GuidedOnboardingStep.SETTINGS_FANDOM -> GuidedOnboardingTarget.SETTINGS_FANDOM
		GuidedOnboardingStep.SETTINGS_DONATE -> GuidedOnboardingTarget.SETTINGS_DONATE
		else -> null
	}

private fun GuidedOnboardingStep.titleRes(): Int =
	when (this) {
		GuidedOnboardingStep.HOME_SETTINGS -> R.string.guided_onboarding_settings_title
		GuidedOnboardingStep.SETTINGS_LANGUAGE -> R.string.guided_onboarding_language_title
		GuidedOnboardingStep.SETTINGS_FANDOM -> R.string.guided_onboarding_fandom_title
		GuidedOnboardingStep.SETTINGS_DONATE -> R.string.guided_onboarding_donate_title
		else -> R.string.guided_onboarding_info_title
	}

private fun GuidedOnboardingStep.messageRes(): Int =
	when (this) {
		GuidedOnboardingStep.HOME_SETTINGS -> R.string.guided_onboarding_settings_message
		GuidedOnboardingStep.SETTINGS_LANGUAGE -> R.string.guided_onboarding_language_message
		GuidedOnboardingStep.SETTINGS_FANDOM -> R.string.guided_onboarding_fandom_message
		GuidedOnboardingStep.SETTINGS_DONATE -> R.string.guided_onboarding_donate_message
		else -> R.string.guided_onboarding_info_message
	}

private fun GuidedOnboardingStep.icon() =
	when (this) {
		GuidedOnboardingStep.HOME_SETTINGS -> Lucide.Settings
		GuidedOnboardingStep.SETTINGS_LANGUAGE -> Lucide.Globe
		GuidedOnboardingStep.SETTINGS_FANDOM -> Lucide.CircleAlert
		GuidedOnboardingStep.SETTINGS_DONATE -> Lucide.Coffee
		else -> Lucide.CircleAlert
	}

private fun Rect.expand(padding: Float): Rect =
	Rect(
		left = left - padding,
		top = top - padding,
		right = right + padding,
		bottom = bottom + padding
	)
