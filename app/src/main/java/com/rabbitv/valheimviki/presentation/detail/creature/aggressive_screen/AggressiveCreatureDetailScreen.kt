package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Heater
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Trophy
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.main_detail_images.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.trident_dividers.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.creature.components.CardWithOverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.OverlayLabel
import com.rabbitv.valheimviki.presentation.detail.creature.components.StatsFlowRow
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.ICON_CLICK_DIM
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE_SECOND
import com.rabbitv.valheimviki.ui.theme.LightDark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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
    val sharedScrollState = rememberScrollState()
    val isExpandable = remember { mutableStateOf(false) }
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
                    Divider(
                        thickness = 1.dp,
                        color = PrimaryWhite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(BODY_CONTENT_PADDING.dp)
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
                    if (uiState.dropItems.isNotEmpty()) {
                        SlavicDivider()
                        CreatureHorizontalPager(
                            rememberPagerState(
                                initialPage = 0,
                                pageCount = { uiState.dropItems.size }),
                            uiState.dropItems,
                            Lucide.Trophy,
                            "Drop Items",
                            "Items that drop from creature after defeating him",
                            ContentScale.Crop,
                            iconModifier = Modifier,
                            parentPageIndex = pageIndex,
                        )
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
                                    uiState.aggressiveCreature.levels[pageIndex].baseHp.toString()
                                        .uppercase(),
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
                    StatsFlowRow(
                        baseDamage = uiState.aggressiveCreature.levels[pageIndex].baseDamage,
                        weakness = uiState.aggressiveCreature.weakness,
                        resistance = uiState.aggressiveCreature.resistance,
                        abilities = uiState.aggressiveCreature.abilities,
                    )
                    SlavicDivider()
                    Box(modifier = Modifier.size(45.dp))
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
fun CreatureHorizontalPager(
    pagerState: PagerState,
    list: List<DropItem>,
    icon: ImageVector,
    title: String,
    subTitle: String,
    contentScale: ContentScale,
    iconModifier: Modifier = Modifier.rotate(-85f),
    parentPageIndex: Int,
) {
    val pageWidth = 300.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(
                start = BODY_CONTENT_PADDING.dp,
                end = BODY_CONTENT_PADDING.dp,
                bottom = BODY_CONTENT_PADDING.dp,
            )
    )
    {
        Column(
            horizontalAlignment = Alignment.Start
        )
        {
            Column(horizontalAlignment = Alignment.Start) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        tint = Color.White,
                        contentDescription = "Rectangle section Icon",
                        modifier = iconModifier
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Text(
                        title.uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp))
                Text(
                    subTitle,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Spacer(modifier = Modifier.padding(6.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                pageSize = PageSize.Fixed(pageWidth),
                beyondViewportPageCount = list.size,
                contentPadding = PaddingValues(end = 40.dp),
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    pagerSnapDistance = PagerSnapDistance.atMost(list.size)
                )
            ) { pageIndex ->
                CreatureHorizontalPagerItem(
                    parentPageIndex = parentPageIndex,
                    pagerState = pagerState,
                    list = list,
                    pageIndex = pageIndex,
                    contentScale = contentScale,
                )
            }
        }
    }
}


@Composable
fun CreatureHorizontalPagerItem(
    pagerState: PagerState,
    list: List<DropItem>,
    pageIndex: Int,
    parentPageIndex: Int,
    contentScale: ContentScale,
) {
    Card(
        Modifier
            .size(280.dp)
            .graphicsLayer {
                val pageOffset = (
                        (pagerState.currentPage - pageIndex) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue
                val scale = lerp(
                    start = 0.8f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
                scaleX = scale
                scaleY = scale

                alpha = lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
                cameraDistance = 8f * density
            }
            .shadow(
                elevation = 8.dp,
                shape = CardDefaults.shape,
                clip = false,
                ambientColor = Color.White.copy(alpha = 0.1f),
                spotColor = Color.White.copy(alpha = 0.25f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = LightDark
        ),
        border = BorderStroke(2.dp, DarkWood),

        ) {
        list.let {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(83.dp)
                        .background(Color.Transparent),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it[pageIndex].material.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.item_grid_image),
                    contentScale = contentScale
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding
                                (horizontal = 5.dp)

                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = it[pageIndex].material.name.toString().uppercase(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = PrimaryWhite,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = Modifier
                            .padding
                                (horizontal = 5.dp)

                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "x${it[pageIndex].quantityList[parentPageIndex].toString()}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = PrimaryWhite,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = Modifier
                            .padding
                                (horizontal = 5.dp)

                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "DROP CHANCE: ${
                            it[pageIndex].chanceStarList[parentPageIndex].toString()
                        }%",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = PrimaryWhite,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

            }

        }
    }
}

@Composable
@Preview("CreatureHorizontalPagerItem")
fun PreviewCreatureHorizontalPagerItem() {
    ValheimVikiAppTheme {
        val pagerState = rememberPagerState(pageCount = {
            10
        })
        val creatureList = FakeData.generateFakeCreatures()
        CreatureHorizontalPagerItem(
            pagerState = pagerState,
            list = emptyList(),
            pageIndex = 1,
            parentPageIndex = 1,
            contentScale = ContentScale.Crop
        )
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
        ),
        abilities = "SDASDAD"
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