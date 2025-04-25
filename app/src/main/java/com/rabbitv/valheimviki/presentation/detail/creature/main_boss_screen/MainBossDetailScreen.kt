package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Heater
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TreePine
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.GreenTorchesDivider
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.trident_dividers.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.BossStatsFlowRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.CardWithImageAndTitle
import com.rabbitv.valheimviki.presentation.detail.creature.components.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.CardWithTrophy
import com.rabbitv.valheimviki.presentation.detail.creature.components.CustomRowLayout
import com.rabbitv.valheimviki.presentation.detail.creature.components.OverlayLabel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainBossDetailScreen(
    onBack: () -> Unit,
    viewModel: MainBossScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val mainBoss by viewModel.mainBoss.collectAsStateWithLifecycle()
    val relatedForsakenAltar by viewModel.relatedForsakenAltar.collectAsStateWithLifecycle()
    val relatedBiome by viewModel.relatedBiome.collectAsStateWithLifecycle()
    val relatedSummoningItems by viewModel.relatedSummoningItems.collectAsStateWithLifecycle()
    val dropItems by viewModel.dropItems.collectAsStateWithLifecycle()
    val trophy by viewModel.trophy.collectAsStateWithLifecycle()
    val sacrificialStones by viewModel.sacrificialStones.collectAsStateWithLifecycle()

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")

    val painter = rememberAsyncImagePainter(relatedBiome?.imageUrl)

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { dropItems.size })

    mainBoss?.let { mainBoss ->
        MainBossContent(
            onBack = onBack,
            animatedVisibilityScope = animatedVisibilityScope,
            mainBoss = mainBoss,
            relatedBiome = relatedBiome,
            relatedForsakenAltar = relatedForsakenAltar,
            relatedSummoningItems = relatedSummoningItems,
            dropItems = dropItems,
            trophyUrl = trophy?.imageUrl,
            sharedTransitionScope = sharedTransitionScope,
            sacrificialStones = sacrificialStones,
            painter = painter,
            pagerState = pagerState
        )
    }

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainBossContent(
    mainBoss: MainBoss,
    relatedBiome: Biome?,
    relatedForsakenAltar: PointOfInterest?,
    relatedSummoningItems: List<Material?>,
    dropItems: List<ItemData?>,
    sacrificialStones: PointOfInterest?,
    trophyUrl: String?,
    pagerState: PagerState,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    errorPainter: Painter? = null,
    painter: AsyncImagePainter
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
                    itemData = mainBoss,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    textAlign = TextAlign.Center
                )
                DetailExpandableText(text = mainBoss.description.toString())
                TridentsDividedRow(text = "BOSS DETAIL")
                relatedBiome?.let {
                    CardWithOverlayLabel(
                        painter = painter,
                        content = {
                            Row {
                                Box(
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    OverlayLabel(
                                        icon = Lucide.TreePine,
                                        label = " PRIMARY SPAWN",
                                    )
                                }
                                Text(
                                    it.name.uppercase(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                    )
                }
                relatedForsakenAltar?.let {
                    TridentsDividedRow()
                    ImageWithTopLabel(
                        itemData = relatedForsakenAltar,
                        horizontalDividerWidth = 250.dp,
                        textStyle = MaterialTheme.typography.titleLarge
                    )
                }
                SlavicDivider()
                relatedSummoningItems.isNotEmpty().let {
                    CardWithOverlayLabel(
                        height = 180.dp,
                        alpha = 0.1f,
                        painter = painterResource(R.drawable.summoning_bg),
                        content = {
                            Column {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OverlayLabel(

                                        icon = Lucide.Flame,
                                        label = "SUMMONING ITEMS",
                                    )
                                }
                                CustomRowLayout(
                                    relatedSummoningItems = relatedSummoningItems,
                                    modifier = Modifier.weight(1f)
                                )

                            }
                        }
                    )
                }
                dropItems.isNotEmpty().let {
                    HorizontalPagerSection(
                        pagerState,
                        dropItems,
                        Lucide.Trophy,
                        "Drop Items",
                        "Items that drop from boss after defeating him",
                        ContentScale.Crop,
                        iconModifier = Modifier
                    )
                }
                GreenTorchesDivider(text = "FORSAKEN POWER")
                Row(
                    modifier = Modifier.padding(BODY_CONTENT_PADDING.dp)
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        CardWithTrophy(
                            forsakenPower = mainBoss.forsakenPower,
                            trophyUrl = trophyUrl
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(15.dp)
                            .wrapContentSize()
                    )
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        sacrificialStones?.let {
                            CardWithImageAndTitle(
                                "WHERE TO HANG THE BOSS TROPHY",
                                imageUrl = sacrificialStones.imageUrl,
                                itemName = sacrificialStones.name,
                                contentScale = ContentScale.Crop,
                            )
                        }

                    }
                }
                TridentsDividedRow(text = "BOSS STATS")
                CardWithOverlayLabel(
                    painter = painterResource(R.drawable.base_hp_bg),
                    content = {
                        Row {
                            Box(
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                OverlayLabel(
                                    icon = Lucide.Heater,
                                    label = " BASE HP",
                                )
                            }
                            Text(
                                mainBoss.baseHP.toString().uppercase(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
                BossStatsFlowRow(item = mainBoss)
                SlavicDivider()
                Box(modifier = Modifier.size(45.dp))
            }
        }
    )
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
    name = "CreatureDetail",
    showBackground = true
)
@Composable
private fun PreviewCreatureDetail() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            MainBossDetailScreen(
                onBack = { },
                animatedVisibilityScope = this
            )
        }
    }

}