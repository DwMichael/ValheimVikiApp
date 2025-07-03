package com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBoss
import com.rabbitv.valheimviki.data.mappers.creatures.toMiniBoss
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.bg_image.BgImage
import com.rabbitv.valheimviki.presentation.components.card.card_image.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.images.FramedImage
import com.rabbitv.valheimviki.presentation.detail.material.boss_drop.model.BossDropUiState
import com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.model.MiniBossDropUiState
import com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.viewmodel.MiniBossDropDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MiniBossDropDetailScreen(
	onBack: () -> Unit,
	viewModel: MiniBossDropDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	MiniBossDropDetailContent(
		uiState = uiState,
		onBack = onBack,
	)

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MiniBossDropDetailContent(
	uiState: MiniBossDropUiState,
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

	BgImage()
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



					if (uiState.miniBoss != null) {
						ImageWithTopLabel(
							itemData = uiState.miniBoss,
							subTitle = "Boss from witch this item drop",
							contentScale = ContentScale.Crop,
						)
						SlavicDivider()
					}

					if (uiState.npc != null) {
						ImageWithTopLabel(
							itemData = uiState.npc,
							subTitle = "Npc that give you quest for this item",
							contentScale = ContentScale.Crop,
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


	ValheimVikiAppTheme {
		MiniBossDropDetailContent(
			uiState = MiniBossDropUiState(
				material = FakeData.generateFakeMaterials()[0],
				miniBoss = FakeData.generateFakeCreatures()[0].toMiniBoss(),
				isLoading = false,
				error = null
			),
			onBack = {},
		)
	}

}
