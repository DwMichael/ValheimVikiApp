package com.rabbitv.valheimviki.presentation.detail.creature.passive_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Atom
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardStatDetails
import com.rabbitv.valheimviki.presentation.detail.creature.components.cards.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.CreatureHorizontalPager
import com.rabbitv.valheimviki.presentation.detail.creature.components.horizontal_pager.CreatureHorizontalPagerData
import com.rabbitv.valheimviki.presentation.detail.creature.components.rows.StarLevelRow
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model.PassiveCreatureDetailUiState
import com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.viewmodel.PassiveCreatureDetailScreenViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import kotlinx.coroutines.launch


@Composable
fun PassiveCreatureDetailScreen(
    onBack: () -> Unit,
    viewModel: PassiveCreatureDetailScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PassiveCreatureDetailContent(
        uiState = uiState,
        onBack = onBack
    )

}


@Composable
fun PassiveCreatureDetailContent(
    onBack: () -> Unit,
    uiState: PassiveCreatureDetailUiState,
) {


    val pagerState =
        rememberPagerState(
            pageCount = { uiState.passiveCreature?.levels?.size ?: 0 },
        )

    val sharedScrollState = rememberScrollState()
    val isExpandable = remember { mutableStateOf(false) }
    val isExpandableNote = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Scaffold { padding ->
        uiState.passiveCreature?.let {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
                beyondViewportPageCount = it.levels.size,
            ) { pageIndex ->
                Column(
                    modifier = Modifier.verticalScroll(sharedScrollState),
                    verticalArrangement = Arrangement.Center,
                ) {
                    MainDetailImage(
                        onBack = onBack,
                        imageUrl = it.levels[pageIndex].image.toString(),
                        name = it.name,
                        textAlign = TextAlign.Center
                    )
                    PageIndicator(pagerState)
                    StarLevelRow(
                        levelsNumber = it.levels.size,
                        pageIndex = pageIndex,
                        paddingValues = PaddingValues(BODY_CONTENT_PADDING.dp),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(BODY_CONTENT_PADDING.dp),
                        thickness = 1.dp,
                        color = PrimaryWhite
                    )
                    DetailExpandableText(
                        text = it.description,
                        collapsedMaxLine = 3,
                        isExpanded = isExpandable
                    )

                    TridentsDividedRow(text = "DETAILS")
                    uiState.biome?.let {
                        Text(
                            modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp),
                            text = "PRIMARY SPAWN",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Visible
                        )
                        CardWithOverlayLabel(
                            painter = rememberAsyncImagePainter(uiState.biome.imageUrl),
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
                    if (uiState.materialDrops.isNotEmpty()) {
                        SlavicDivider()
                        CreatureHorizontalPager(
                            rememberPagerState(
                                initialPage = 0,
                                pageCount = { uiState.materialDrops.size }),
                            uiState.materialDrops,
                            CreatureHorizontalPagerData(
                                title = "Drop Items",
                                subTitle = "Items that drop from creature after defeating him",
                                icon = Lucide.Trophy,
                                iconRotationDegrees = -85f,
                                contentScale = ContentScale.Crop,
                                parentPageIndex = pageIndex,
                            )
                        )
                    }

                    TridentsDividedRow(text = "BOSS STATS")
                    CardStatDetails(
                        title = stringResource(R.string.baseHp),
                        text = uiState.passiveCreature.levels[pageIndex].baseHp.toString(),
                        icon = Icons.Outlined.Favorite,
                        iconColor = Color.Red,
                        styleTextFirst = MaterialTheme.typography.labelSmall,
                        styleTextSecond = MaterialTheme.typography.bodyLarge,
                        iconSize = 36.dp
                    )
                    Spacer(Modifier.height(10.dp))
                    CardStatDetails(
                        title = stringResource(R.string.fun_fact),
                        text = uiState.passiveCreature.abilities.toString(),
                        icon = Lucide.Atom,
                        iconColor = Color.White,
                        styleTextFirst = MaterialTheme.typography.labelSmall,
                        styleTextSecond = MaterialTheme.typography.bodyMedium,
                        iconSize = 36.dp
                    )
                    if (uiState.passiveCreature.notes.isNotBlank()) {
                        SlavicDivider()
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 10.dp),
                            text = "NOTE",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        DetailExpandableText(
                            text = it.notes,
                            collapsedMaxLine = 3,
                            isExpanded = isExpandableNote
                        )
                    }
                    SlavicDivider()
                    Box(modifier = Modifier.size(45.dp))
                }
            }
        }
    }
}


@Composable
fun PageIndicator(
    pagerState: PagerState,
) {
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )
        }
    }
}


@Preview(name = "CreaturePage")
@Composable
fun PreviewCreaturePage() {
    AggressiveCreature(
        id = "1",
        category = "asd",
        subCategory = "sdasd",
        imageUrl = "sadasd",
        name = "sadsdd",
        description = "asdasd2",
        order = 2,
        weakness = "SDASD",
        resistance = "dasdas2",
        baseDamage = "dsasdasd",
        levels = listOf(
            LevelCreatureData(
                level = 1,
                baseHp = 23123,
                baseDamage = "DAsdasd",
                image = "dsadasd"
            )
        ),
        abilities = "SDASDAD"
    )
    remember { mutableStateOf(true) }

}

