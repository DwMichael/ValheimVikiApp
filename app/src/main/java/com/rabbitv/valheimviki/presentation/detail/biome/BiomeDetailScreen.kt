package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.composables.icons.lucide.Pickaxe
import com.composables.icons.lucide.Trees
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.ui_state.detail.biome_detail_state.UiBiomeDetailState
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.biome.viewmodel.BiomeDetailScreenViewModel
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BiomeDetailScreen(
    onBack: () -> Unit,
    viewModel: BiomeDetailScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val biomeUiState by viewModel.biomeUiState.collectAsStateWithLifecycle()
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")

    BiomeDetailContent(
        onBack = onBack,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        biomeUiState = biomeUiState
    )

}


@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun BiomeDetailContent(
    onBack: () -> Unit,
    biomeUiState: UiBiomeDetailState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
    val scrollState = rememberScrollState()
    val creatureData = HorizontalPagerData(
        title = "Creatures",
        subTitle = "Creatures you may encounter in this biome",
        icon = Lucide.PawPrint,
        iconRotationDegrees = -85f,
        itemContentScale = ContentScale.Crop
    )
    val oreDepositData = HorizontalPagerData(
        title = "Ore Deposits",
        subTitle = "Ore Deposits you may encounter in this biome",
        icon = Lucide.Pickaxe,
        iconRotationDegrees = -85f,
        itemContentScale = ContentScale.Crop,
    )

    val materialData = HorizontalPagerData(
        title = "Materials",
        subTitle = "Unique materials you may encounter in this biome",
        icon = Lucide.Gem,
        iconRotationDegrees = -85f,
        itemContentScale = ContentScale.Crop,
    )

    val pointOfInterestData = HorizontalPagerData(
        title = "Points Of Interest",
        subTitle = "Points Of Interest you may encounter in this biome",
        icon = Lucide.House,
        iconRotationDegrees = -85f,
        itemContentScale = ContentScale.Crop,
    )
    val treeData = HorizontalPagerData(
        title = "Trees",
        subTitle = "Trees you may encounter in this biome",
        icon = Lucide.Trees,
        iconRotationDegrees = -85f,
        itemContentScale = ContentScale.Crop,
    )
    Scaffold(
        content = { padding ->
            AnimatedContent(
                targetState = biomeUiState,
                modifier = Modifier.fillMaxSize(),
                transitionSpec = {
                    if (targetState is UiBiomeDetailState.Success && initialState is UiBiomeDetailState.Loading) {
                        fadeIn(
                            animationSpec = tween(
                                durationMillis = 650,
                                delayMillis = 0
                            )
                        ) + slideInVertically(
                            initialOffsetY = { height -> height / 25 },
                            animationSpec = tween(
                                durationMillis = 650,
                                delayMillis = 0,
                                easing = EaseOutCubic
                            )
                        ) togetherWith
                                fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 200
                                    )
                                )
                    } else {
                        fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith
                                fadeOut(animationSpec = tween(durationMillis = 300))
                    }
                },
                label = "LoadingStateTransition"
            ) { currentState ->
                when (currentState) {
                    UiBiomeDetailState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.size(45.dp))
                        }
                    }

                    is UiBiomeDetailState.Success -> {
                        val biome = currentState.biome
                        if (biome != null) {
                            Image(
                                painter = painterResource(id = R.drawable.main_background),
                                contentDescription = "BackgroundImage",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                            Column(
                                modifier = Modifier
                                    .testTag("BiomeDetailScreen")
                                    .fillMaxSize()
                                    .padding(padding)
                                    .verticalScroll(scrollState, enabled = !isRunning),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start,
                            ) {
                                MainDetailImageAnimated(
                                    onBack = onBack,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    id = biome.id,
                                    imageUrl = biome.imageUrl,
                                    title = biome.name
                                )

                                DetailExpandableText(text = biome.description.toString())
                                currentState.mainBoss?.let { mainBoss ->
                                    ImageWithTopLabel(
                                        itemData = mainBoss,
                                        subTitle = "BOSS",
                                    )
                                }
                                SlavicDivider()
                                if (currentState.relatedCreatures.isNotEmpty()) {
                                    HorizontalPagerSection(
                                        list = currentState.relatedCreatures,
                                        data = creatureData,
                                    )
                                }

                                if (currentState.relatedOreDeposits.isNotEmpty()) {
                                    TridentsDividedRow()
                                    HorizontalPagerSection(
                                        list = currentState.relatedOreDeposits,
                                        data = oreDepositData
                                    )
                                }

                                if (currentState.relatedMaterials.isNotEmpty()) {
                                    TridentsDividedRow()
                                    HorizontalPagerSection(
                                        list = currentState.relatedMaterials,
                                        data = materialData
                                    )
                                }
                                if (currentState.relatedPointOfInterest.isNotEmpty()) {
                                    TridentsDividedRow()
                                    HorizontalPagerSection(
                                        list = currentState.relatedPointOfInterest,
                                        data = pointOfInterestData
                                    )
                                }
                                if (currentState.relatedTrees.isNotEmpty()) {
                                    TridentsDividedRow()
                                    HorizontalPagerSection(
                                        list = currentState.relatedTrees,
                                        data = treeData
                                    )
                                }
                                Box(modifier = Modifier.size(45.dp))
                            }
                        }
                    }

                    is UiBiomeDetailState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Error occurred: ${currentState.message}")
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview("BiomeDetailContent", showBackground = true)
@Composable
fun PreviewBiomeDetailContent() {
    val fakeBiome = Biome(
        id = "1",
        imageUrl = "https://via.placeholder.com/600x320.png?text=Biome+Image",
        name = "Przykładowy Biome",
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
        category = "BIOME",
        order = 1
    )
    val fakeMainBoss = MainBoss(
        id = "boss1",
        imageUrl = "https://via.placeholder.com/400x200.png?text=MainBoss+Image",
        category = "CREATURE",
        subCategory = "BOSS",
        name = "Przykładowy MainBoss",
        description = "Przykładowy opis głównego bossa.",
        order = 1,
        baseHP = 1500,
        weakness = "Ogień",
        resistance = "Lód",
        baseDamage = "100",
        collapseImmune = "False",
        forsakenPower = "High"
    )
    val creatureList = FakeData.generateFakeCreatures()
    val oreDeposit = FakeData.generateFakeOreDeposits()
    val materials = FakeData.generateFakeMaterials()
    val uiState = UiBiomeDetailState.Success(
        biome = fakeBiome,
        mainBoss = fakeMainBoss,
        relatedCreatures = creatureList,
        relatedOreDeposits = oreDeposit,
        relatedMaterials = materials,
        relatedPointOfInterest = emptyList(),
        relatedTrees = emptyList()
    )

    ValheimVikiAppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                BiomeDetailContent(
                    onBack = { },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    biomeUiState = uiState
                )
            }
        }
    }
}

