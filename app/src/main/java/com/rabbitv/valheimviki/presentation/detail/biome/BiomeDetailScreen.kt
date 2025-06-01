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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.biome.model.BiomeDetailUiState
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
    val biomeUiState by viewModel.uiState.collectAsStateWithLifecycle()
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
    biomeUiState: BiomeDetailUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
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
    val  treeData = HorizontalPagerData(
        title = "Trees",
        subTitle =  "Trees you may encounter in this biome",
        icon = Lucide.Trees,
        iconRotationDegrees = -85f,
        itemContentScale = ContentScale.Crop,
    )
    Scaffold(
        content = { padding ->
            val scrollState = rememberScrollState()

            AnimatedContent(
                targetState = biomeUiState.isLoading,
                modifier = Modifier.fillMaxSize(),
                transitionSpec = {
                    if (targetState == false && initialState == true) {
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
            ) { isLoading ->
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(45.dp))
                    }
                } else if (biomeUiState.biome != null) {
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
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        MainDetailImageAnimated(
                            onBack = onBack,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            id = biomeUiState.biome.id,
                            imageUrl = biomeUiState.biome.imageUrl,
                            title = biomeUiState.biome.name
                        )

                        DetailExpandableText(text = biomeUiState.biome.description.toString())
                        biomeUiState.mainBoss?.let { mainBoss ->
                            ImageWithTopLabel(
                                itemData = mainBoss,
                                subTitle = "BOSS",
                            )
                        }
                        SlavicDivider()
                        if (biomeUiState.relatedCreatures.isNotEmpty()) {
                            HorizontalPagerSection(
                                list = biomeUiState.relatedCreatures,
                                data = creatureData,
                            )
                        }

                        if (biomeUiState.relatedOreDeposits.isNotEmpty()) {
                            TridentsDividedRow()
                            HorizontalPagerSection(
                                list = biomeUiState.relatedOreDeposits,
                                data = oreDepositData
                            )
                        }

                        if (biomeUiState.relatedMaterials.isNotEmpty()) {
                            TridentsDividedRow()
                            HorizontalPagerSection(
                                list = biomeUiState.relatedMaterials,
                              data = materialData
                            )
                        }
                        if (biomeUiState.relatedPointOfInterest.isNotEmpty()) {
                            TridentsDividedRow()
                            HorizontalPagerSection(
                                list = biomeUiState.relatedPointOfInterest,
                                data = pointOfInterestData
                            )
                        }
                        if (biomeUiState.relatedTrees.isNotEmpty()) {
                            TridentsDividedRow()
                            HorizontalPagerSection(
                                list = biomeUiState.relatedTrees,
                                data = treeData
                            )
                        }
                        Box(modifier = Modifier.size(45.dp))
                    }
                } else if (biomeUiState.error != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Wystąpił błąd: ${biomeUiState.error}")
                    }
                } else {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                    )
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
    val uiState = BiomeDetailUiState(
        biome = fakeBiome,
        mainBoss = fakeMainBoss,
        relatedCreatures = creatureList,
        relatedOreDeposits = oreDeposit,
        relatedMaterials = materials,
        relatedPointOfInterest = listOf(),
        relatedTrees = listOf(),
        isLoading = false,
        error = null
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

