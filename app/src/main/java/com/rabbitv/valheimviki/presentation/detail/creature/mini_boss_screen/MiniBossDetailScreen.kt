package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Heater
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.main_detail_images.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_dividers.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.BossStatsFlowRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.OverlayLabel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import kotlinx.coroutines.delay


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniBossDetailScreen(
    onBack: () -> Unit,
    viewModel: MiniBossDetailScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val miniBossUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")

    MiniBossContent(
        onBack = onBack,
        miniBossUiSate = miniBossUiState,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniBossContent(
    miniBossUiSate: MiniBossDetailUiState,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val transitionComplete = remember { mutableStateOf(false) }
    val scrollEnabled = remember { derivedStateOf { transitionComplete.value } }

    LaunchedEffect(Unit) {
        delay(700)
        transitionComplete.value = true
    }

    when {
        miniBossUiSate.isLoading -> {
            Box(modifier = Modifier.size(45.dp))
        }

        miniBossUiSate.miniBoss != null -> {
            val scrollModifier = if (scrollEnabled.value) {
                Modifier.verticalScroll(rememberScrollState())
            } else {
                Modifier
            }
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
                            .then(scrollModifier),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        MainDetailImageAnimated(
                            onBack = onBack,
                            itemData = miniBossUiSate.miniBoss,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            textAlign = TextAlign.Center
                        )
                        DetailExpandableText(text = miniBossUiSate.miniBoss.description.toString())
                        TridentsDividedRow(text = "BOSS DETAIL")
                        miniBossUiSate.primarySpawn?.let {
                            Text(
                                modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp),
                                text = "PRIMARY SPAWN",
                                textAlign = TextAlign.Left,
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Visible
                            )
                            CardWithOverlayLabel(
                                painter = rememberAsyncImagePainter(miniBossUiSate.primarySpawn.imageUrl),
                                content = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .wrapContentHeight(Alignment.CenterVertically)
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    ) {
                                        Text(
                                            it.name.uppercase(),
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier,
                                            color = Color.White,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            )
                        }
                        if (miniBossUiSate.dropItems.isNotEmpty()) {
                            SlavicDivider()
                            HorizontalPagerSection(
                                rememberPagerState(
                                    initialPage = 1,
                                    pageCount = { miniBossUiSate.dropItems.size }),
                                miniBossUiSate.dropItems,
                                Lucide.Trophy,
                                "Drop Items",
                                "Items that drop from boss after defeating him",
                                ContentScale.Crop,
                                iconModifier = Modifier
                            )
                        }
                        if (miniBossUiSate.dropItems.isEmpty() && miniBossUiSate.primarySpawn == null) {
                            Row(modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp))
                            {
                                Spacer(Modifier.weight(1f))
                                Text(
                                    "Connect to Internet to fetch boss detail data",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(2f),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.weight(1f))
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
                                        miniBossUiSate.miniBoss.baseHP.toString().uppercase(),
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
                        BossStatsFlowRow(item = miniBossUiSate.miniBoss)
                        SlavicDivider()
                        Box(modifier = Modifier.size(45.dp))

                    }


                }
            )
        }

        miniBossUiSate.error != null -> {
            Box(modifier = Modifier.size(45.dp))
        }
    }
}
