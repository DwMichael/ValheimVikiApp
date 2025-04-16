package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.trident_dividers.RowTwoTridentDividers
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData

const val DEFAULT_MINIMUM_TEXT_LINE = 4
const val BODY_CONTENT_PADDING = 10

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BiomeDetailScreen(
    onBack: () -> Unit,
    viewModel: BiomeDetailScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val biome by viewModel.biome.collectAsStateWithLifecycle()
    val mainBoss by viewModel.mainBoss.collectAsStateWithLifecycle()
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val relatedCreatures by viewModel.relatedCreatures.collectAsStateWithLifecycle()
    val relatedOreDeposits by viewModel.relatedOreDeposits.collectAsStateWithLifecycle()
    val relatedMaterials by viewModel.relatedMaterials.collectAsStateWithLifecycle()
    val relatedPointOfInterest by viewModel.relatedPointOfInterest.collectAsStateWithLifecycle()
    val relatedTrees by viewModel.relatedTrees.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { relatedCreatures.size })
    val pagerState2 = rememberPagerState(
        initialPage = 1,
        pageCount = { relatedOreDeposits.size })
    val pagerState3 = rememberPagerState(
        initialPage = 1,
        pageCount = { relatedMaterials.size })
    val pagerState4 = rememberPagerState(initialPage = 1,pageCount = { relatedPointOfInterest.size })
    val pagerState5 = rememberPagerState(initialPage = 1,pageCount = { relatedTrees.size })

    biome?.let { biome ->
        BiomeDetailContent(
            biome = biome,
            mainBoss = mainBoss,
            onBack = onBack,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            pagerState = pagerState,
            pagerState2 = pagerState2,
            pagerState3 = pagerState3,
            pagerState4 = pagerState4,
            pagerState5= pagerState5,
            relatedCreatures = relatedCreatures,
            relatedOreDeposits = relatedOreDeposits,
            relatedMaterials= relatedMaterials,
            relatedPointOfInterest = relatedPointOfInterest,
            relatedTrees = relatedTrees
        )
    }
}



@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BiomeDetailContent(
    biome: Biome,
    mainBoss: MainBoss?,
    onBack: () -> Unit,
    pagerState: PagerState,
    pagerState2: PagerState,
    pagerState3: PagerState,
    pagerState4: PagerState,
    pagerState5: PagerState,
    relatedCreatures: List<Creature>,
    relatedOreDeposits: List<OreDeposit>,
    relatedMaterials: List<Material>,
    relatedPointOfInterest:List<PointOfInterest>,
    relatedTrees:List<Tree>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    errorPainter: Painter? = null,
) {
    Scaffold(
        content = { padding ->
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                MainDetailImage(
                    onBack = onBack,
                    itemData = biome,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    errorPainter = errorPainter
                )
                DetailExpandableText(text = biome.description.toString())
                mainBoss?.let { mainBoss ->
                    ImageWithTopLabel(itemData = mainBoss, errorPainter = errorPainter)
                }
                SlavicDivider()
                if(relatedCreatures.isNotEmpty()) {
                    HorizontalPagerSection(
                        pagerState, relatedCreatures ,
                        Lucide.PawPrint,
                        "Creatures",
                        "Creatures you may encounter in this biome",
                        ContentScale.Crop,
                    )
                }

                if(relatedOreDeposits.isNotEmpty()) {
                    RowTwoTridentDividers()
                    HorizontalPagerSection(
                        pagerState2, relatedOreDeposits ,
                        Lucide.Pickaxe,
                        "Ore Deposits",
                        "Ore Deposits you may encounter in this biome",
                        ContentScale.Crop,
                    )
                }

                if(relatedMaterials.isNotEmpty()) {
                    RowTwoTridentDividers()
                    HorizontalPagerSection(
                        pagerState3, relatedMaterials ,
                        Lucide.Gem,
                        "Materials",
                        "Unique materials you may encounter in this biome",
                        ContentScale.Crop,
                        iconModifier = Modifier
                    )
                }
                if(relatedPointOfInterest.isNotEmpty()) {
                    RowTwoTridentDividers()
                    HorizontalPagerSection(
                        pagerState4, relatedPointOfInterest ,
                        Lucide.House,
                        "Points Of Interest",
                        "Points Of Interest you may encounter in this biome",
                        ContentScale.Crop,
                        iconModifier = Modifier
                    )
                }
                if(relatedPointOfInterest.isNotEmpty()) {
                    RowTwoTridentDividers()
                    HorizontalPagerSection(
                        pagerState5, relatedTrees ,
                        Lucide.Trees,
                        "Trees",
                        "Trees you may encounter in this biome",
                        ContentScale.Crop,
                        iconModifier = Modifier
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class) // Dodaj tę adnotację na poziomie pliku lub funkcji
@Preview("BiomeDetailContent", showBackground = true)
@Composable
fun PreviewBiomeDetailContent() {
    // Przykładowe dane do podglądu
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
        levels = 2,
        baseHP = 1500,
        weakness = "Ogień",
        resistance = "Lód",
        baseDamage = "100",
        collapseImmune = "False",
        forsakenPower = "High"
    )
    val pagerState = rememberPagerState(pageCount = {
        10
    })
    val pagerState2 = rememberPagerState(pageCount = {
        5
    })
    val pagerState3 = rememberPagerState(pageCount = {
        5
    })
    val creatureList = FakeData.generateFakeCreatures()
    val oreDeposit = FakeData.generateFakeOreDeposits()
    val materials = FakeData.generateFakeMaterials()
    ValheimVikiAppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                BiomeDetailContent(
                    biome = fakeBiome,
                    mainBoss = fakeMainBoss,
                    onBack = { },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    errorPainter = painterResource(R.drawable.preview_image),
                    pagerState = pagerState,
                    pagerState2 = pagerState2,
                    pagerState3 = pagerState3,
                    relatedCreatures = creatureList,
                    relatedOreDeposits = oreDeposit,
                    relatedMaterials = materials,
                    relatedPointOfInterest = emptyList(),
                    pagerState4 = pagerState3,
                    pagerState5 = pagerState2,
                    relatedTrees = emptyList(),
                )
            }
        }
    }
}

