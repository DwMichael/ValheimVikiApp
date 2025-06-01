package com.rabbitv.valheimviki.presentation.detail.creature.mini_boss_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerData
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardStatDetails
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.StatsFlowRow
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING


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
        animatedVisibilityScope = animatedVisibilityScope
    )
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniBossContent(
    miniBossUiSate: MiniBossDetailUiState,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val isRunning by remember { derivedStateOf { animatedVisibilityScope.transition.isRunning } }
    val scrollState = rememberScrollState()
    val dropItemData = HorizontalPagerData(
        title = "Drop Items",
        subTitle = "Items that drop from boss after defeating him",
        icon = Lucide.Trophy,
        iconRotationDegrees = 0f,
        itemContentScale = ContentScale.Crop,
    )

    Scaffold(
        content = { padding ->
            AnimatedContent(
                targetState = miniBossUiSate.isLoading,
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
                } else if (miniBossUiSate.miniBoss != null) {
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
                            id = miniBossUiSate.miniBoss.id,
                            imageUrl = miniBossUiSate.miniBoss.imageUrl,
                            title = miniBossUiSate.miniBoss.name
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
                                list = miniBossUiSate.dropItems,
                                data = dropItemData
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
                        Box(
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        {
                            CardStatDetails(
                                title = stringResource(R.string.baseHp),
                                text = miniBossUiSate.miniBoss.baseHP.toString(),
                                icon = Icons.Outlined.Favorite,
                                iconColor = Color.Red,
                                styleTextFirst = MaterialTheme.typography.labelSmall,
                                styleTextSecond = MaterialTheme.typography.bodyLarge,
                                iconSize = 36.dp
                            )
                        }
                        StatsFlowRow(
                            baseDamage = miniBossUiSate.miniBoss.baseDamage,
                            weakness = miniBossUiSate.miniBoss.weakness,
                            resistance = miniBossUiSate.miniBoss.resistance,
                            collapseImmune = miniBossUiSate.miniBoss.collapseImmune,
                        )
                        SlavicDivider()
                        Box(modifier = Modifier.size(45.dp))
                    }
                }
            }

        }
    )
}

