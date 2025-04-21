package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Heater
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.GreenTorchesDivider
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.trident_dividers.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.BossStatsFlowRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.OverlayLabel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniBossDetailScreen(
    onBack: () -> Unit,
    viewModel: MiniBossDetailScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val miniBoss by viewModel.miniBoss.collectAsStateWithLifecycle()
    val dropItems by viewModel.dropItems.collectAsStateWithLifecycle()
    val trophy by viewModel.trophy.collectAsStateWithLifecycle()
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { dropItems.size })

    miniBoss?.let { mainBoss ->
        MiniBossContent(
            onBack = onBack,
            miniBoss = miniBoss!!,
            dropItems = dropItems,
            trophyUrl = trophy?.imageUrl,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            pagerState = pagerState
        )
    }

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniBossContent(
    miniBoss: MiniBoss,
    dropItems: List<ItemData?>,
    trophyUrl: String?,
    pagerState: PagerState,
    onBack: () -> Unit,
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
                    itemData = miniBoss,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    errorPainter = errorPainter,
                    textAlign = TextAlign.Center
                )
                DetailExpandableText(text = miniBoss.description.toString())
                TridentsDividedRow(text = "BOSS DETAIL")

                SlavicDivider()
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
                                miniBoss.baseHP.toString().uppercase(),
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
                BossStatsFlowRow(item = miniBoss)
                SlavicDivider()
                Box(modifier = Modifier.size(45.dp))
            }
        }
    )
}