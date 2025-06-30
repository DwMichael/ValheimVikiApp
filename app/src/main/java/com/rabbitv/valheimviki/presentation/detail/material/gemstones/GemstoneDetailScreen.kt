package com.rabbitv.valheimviki.presentation.detail.material.gemstones


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Cat
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreatures
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreatures
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.material.gemstones.model.GemstoneDetailUiState
import com.rabbitv.valheimviki.presentation.detail.material.gemstones.viewmodel.GemstoneDetailViewModel
import com.rabbitv.valheimviki.presentation.detail.material.offerings.model.OfferingUiState
import com.rabbitv.valheimviki.presentation.detail.material.offerings.viewmodel.OfferingsDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun GemstoneDetailScreen(
	onBack: () -> Unit,
	viewModel: GemstoneDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	GemstoneDetailContent(
		uiState = uiState,
		onBack = onBack,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun GemstoneDetailContent(
	uiState: GemstoneDetailUiState,
	onBack: () -> Unit,
) {

	val scrollState = rememberScrollState()
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

	val isExpandable = remember { mutableStateOf(false) }
	val painterBackgroundImage = painterResource(R.drawable.main_background)

	val pointsOfInterestData = HorizontalPagerData(
		title = "Points of interest",
		subTitle = "Poi where you can find this item",
		icon = Lucide.PawPrint,
		iconRotationDegrees = -85f,
		itemContentScale = ContentScale.Crop
	)


	Image(
		painter = painterBackgroundImage,
		contentDescription = "bg",
		contentScale = ContentScale.FillBounds,
		modifier = Modifier.fillMaxSize()
	)

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		containerColor = Color.Transparent,
		contentColor = PrimaryWhite
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			uiState.material?.let { material ->
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(scrollState)
						.padding(
							top = 20.dp,
							bottom = 70.dp
						),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top,

					) {
					FramedImage(material.imageUrl)
					Text(
						material.name,
						modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
						style = MaterialTheme.typography.displayMedium,
						textAlign = TextAlign.Center
					)
					SlavicDivider()

					material.description?.let {
						DetailExpandableText(
							text = material.description,
							boxPadding = BODY_CONTENT_PADDING.dp,
							isExpanded = isExpandable
						)
						SlavicDivider()
					}
					if (uiState.pointsOfInterest.isNotEmpty()) {
						HorizontalPagerSection(
							list = uiState.pointsOfInterest,
							data = pointsOfInterestData,
						)
					}

				}
			}
			AnimatedVisibility(
				visible = backButtonVisibleState,
				enter = fadeIn(),
				exit = fadeOut(),
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(16.dp)
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
	}
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview("ToolDetailContentPreview", showBackground = true)
@Composable
fun PreviewToolDetailContentCooked() {

	val agg = listOf(
		Creature(
			id = "creature002",
			category = "Aggressive",
			subCategory = "Undead",
			imageUrl = "https://example.com/frost_draugr.png",
			name = "Frost Draugr",
			description = "An ancient warrior risen from the dead in the frozen mountains. Carries ice-encrusted weapons and armor.",
			order = 2,
			levels = 2,
			baseHP = 150,
			weakness = "Fire, Spirit",
			resistance = "Frost, Poison",
			baseDamage = "35-45",
		)
	)
	ValheimVikiAppTheme {
		GemstoneDetailContent(
			uiState = GemstoneDetailUiState(
				material = FakeData.generateFakeMaterials()[0],
				pointsOfInterest = FakeData.pointOfInterest,
				isLoading = false,
				error = null
			),
			onBack = {},
		)
	}

}
