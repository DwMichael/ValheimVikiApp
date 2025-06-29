package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TreePine
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.GreenTorchesDivider

import com.rabbitv.valheimviki.presentation.components.card.card_image.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.horizontal_pager.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardStatDetails
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithImageAndTitle
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithTrophy
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.OverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.CustomRowLayout
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.StatsFlowRow
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model.MainBossDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.viewmodel.MainBossScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainBossDetailScreen(
    onBack: () -> Unit,
    viewModel: MainBossScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val mainBossUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    MainBossContent(
        onBack = onBack,
        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransitionScope = sharedTransitionScope,
        mainBossUiState = mainBossUiState
    )

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainBossContent(
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    mainBossUiState: MainBossDetailUiState,
) {
    val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
    val scrollState = rememberScrollState()
    val dropData = HorizontalPagerData(
        title = "Drop Items",
        subTitle = "Items that drop from boss after defeating him",
        icon = Lucide.Trophy,
        iconRotationDegrees = 0f,
        itemContentScale = ContentScale.Crop
    )

    Scaffold(
        content = { padding ->
            AnimatedContent(
                targetState = mainBossUiState.isLoading,
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
                } else if (mainBossUiState.mainBoss != null) {
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
                            textAlign = TextAlign.Center,
                            id = mainBossUiState.mainBoss.id,
                            imageUrl = mainBossUiState.mainBoss.imageUrl,
                            title = mainBossUiState.mainBoss.name
                        )
                        DetailExpandableText(text = mainBossUiState.mainBoss.description.toString())
                        TridentsDividedRow(text = "BOSS DETAIL")
                        mainBossUiState.relatedBiome?.let {
                            CardWithOverlayLabel(
                                painter = rememberAsyncImagePainter(mainBossUiState.relatedBiome.imageUrl),
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
                        mainBossUiState.relatedForsakenAltar?.let {
                            TridentsDividedRow()
                            ImageWithTopLabel(
                                itemData = mainBossUiState.relatedForsakenAltar,
                                horizontalDividerWidth = 250.dp,
                                textStyle = MaterialTheme.typography.titleLarge
                            )
                        }
                        SlavicDivider()
                        mainBossUiState.relatedSummoningItems.isNotEmpty().let {
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
                                            relatedSummoningItems = mainBossUiState.relatedSummoningItems,
                                            modifier = Modifier.weight(1f)
                                        )

                                    }
                                }
                            )
                        }
                        mainBossUiState.dropItems.isNotEmpty().let {
                            HorizontalPagerSection(
                                list = mainBossUiState.dropItems,
                                data = dropData
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
                                    forsakenPower = mainBossUiState.mainBoss.forsakenPower,
                                    trophyUrl = mainBossUiState.trophy?.imageUrl
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
                                mainBossUiState.sacrificialStones?.let {
                                    CardWithImageAndTitle(
                                        "WHERE TO HANG THE BOSS TROPHY",
                                        imageUrl = mainBossUiState.sacrificialStones.imageUrl,
                                        itemName = mainBossUiState.sacrificialStones.name,
                                        contentScale = ContentScale.Crop,
                                    )
                                }

                            }
                        }
                        TridentsDividedRow(text = "BOSS STATS")
                        Box(
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        {
                            CardStatDetails(
                                title = stringResource(R.string.baseHp),
                                text = mainBossUiState.mainBoss.baseHP.toString(),
                                icon = Icons.Outlined.Favorite,
                                iconColor = Color.Red,
                                styleTextFirst = MaterialTheme.typography.labelSmall,
                                styleTextSecond = MaterialTheme.typography.bodyLarge,
                                iconSize = 36.dp
                            )
                        }
                        StatsFlowRow(
                            baseDamage = mainBossUiState.mainBoss.baseDamage,
                            weakness = mainBossUiState.mainBoss.weakness,
                            resistance = mainBossUiState.mainBoss.resistance,
                            collapseImmune = mainBossUiState.mainBoss.collapseImmune,
                        )
                        SlavicDivider()
                        Box(modifier = Modifier.size(45.dp))
                    }
                }


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