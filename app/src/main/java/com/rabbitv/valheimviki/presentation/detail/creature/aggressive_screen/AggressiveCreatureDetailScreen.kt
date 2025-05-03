package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Star
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.presentation.components.main_detail_images.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.trident_dividers.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.CardWithOverlayLabel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ICON_CLICK_DIM
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE_SECOND
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AggressiveCreatureDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AggressiveCreatureDetailScreenViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AggressiveCreatureDetailContent(
        uiState = uiState,
        onBack = onBack
    )

}


@Composable
fun AggressiveCreatureDetailContent(
    onBack: () -> Unit,
    uiState: AggressiveCreatureDetailUiState,
) {
    val backButtonVisibleState = remember { mutableStateOf(false) }
    mutableListOf<String?>(uiState.aggressiveCreature?.imageUrl)
    LaunchedEffect(Unit) {
        delay(450)
        backButtonVisibleState.value = true
    }

    val pagerState =
        rememberPagerState(
            pageCount = { uiState.aggressiveCreature?.levels?.size ?: 0 },

            )

    val coroutineScope = rememberCoroutineScope()
    Scaffold { padding ->
        uiState.aggressiveCreature?.let {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
                beyondViewportPageCount = it.levels.size,
            ) { pageIndex ->
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
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
                    Divider(
                        thickness = 1.dp,
                        color = PrimaryWhite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(BODY_CONTENT_PADDING.dp)
                    )
                    Text(
                        modifier = Modifier.padding(BODY_CONTENT_PADDING.dp),
                        text = it.description,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge
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
//                    if (uiState.dropItems.isNotEmpty()) {
//                        SlavicDivider()
//                        HorizontalPagerSection(
//                            rememberPagerState(
//                                initialPage = 1,
//                                pageCount = { uiState.dropItems.size }),
//                            uiState.dropItems,
//                            Lucide.Trophy,
//                            "Drop Items",
//                            "Items that drop from boss after defeating him",
//                            ContentScale.Crop,
//                            iconModifier = Modifier
//                        )
//                    }
                }
            }
        }
    }
}


@Composable
fun StarLevelRow(
    onClick: (level: Int) -> Unit,
    levelsNumber: Int,
    pageIndex: Int,
    paddingValues: PaddingValues
) {

    Row(
        Modifier.padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = "Levels :",
            style = MaterialTheme.typography.titleLarge
        )
        repeat(levelsNumber) {
            if (pageIndex + 1 <= it) {
                StarLevelButton(
                    starNumber = it,
                    isFilled = false,
                    onClick = onClick,
                )
            } else {
                StarLevelButton(
                    starNumber = it,
                    isFilled = true,
                    onClick = onClick
                )
            }

        }


    }

}


@Composable
fun StarLevelButton(
    onClick: (level: Int) -> Unit,
    starNumber: Int,
    isFilled: Boolean,
) {
    IconButton(
        onClick = {
            onClick(starNumber)
        },
        modifier = Modifier
            .size(ICON_CLICK_DIM)
    ) {
        if (isFilled) {

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star",
                modifier = Modifier.size(ICON_SIZE_SECOND),
                tint = Color.Yellow
            )

        } else {
            Icon(
                imageVector = Lucide.Star,
                contentDescription = "Star",
                modifier = Modifier.size(ICON_SIZE_SECOND),
                tint = Color.Gray
            )

        }
    }

}

@Preview(name = "StarLevelButton", showBackground = true)
@Composable
fun PreviewStarLevelButton() {
    ValheimVikiAppTheme {
        StarLevelButton(
            starNumber = 0,
            isFilled = false,
            onClick = {},
        )
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
        baseHP = 1111,
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
        )
    )
    remember { mutableStateOf(true) }

}


//@Preview(name = "AggressiveCreatureDetailScreen")
//@Composable
//private fun PreviewAggressiveCreatureDetailScreen() {
//    AggressiveCreatureDetailContent(
//        uiState = AggressiveCreatureDetailUiState()
//    )
//}