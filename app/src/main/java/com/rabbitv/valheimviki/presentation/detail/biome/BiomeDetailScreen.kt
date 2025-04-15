package com.rabbitv.valheimviki.presentation.detail.biome

import android.graphics.Path
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PawPrint
import com.composables.icons.lucide.Pickaxe
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.TridentDivider
import com.rabbitv.valheimviki.ui.theme.DETAIL_ITEM_SHAPE_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData
import kotlin.math.absoluteValue

const val DEFAULT_MINIMUM_TEXT_LINE = 4
const val BODY_CONTENT_PADDING = 10
const val BODY_CONTENT_TOP_PADDING = 20

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
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { relatedCreatures.size })
    val pagerState2 = rememberPagerState(
        initialPage = 1,
        pageCount = { relatedOreDeposits.size })
    val pagerState3 = rememberPagerState(
        initialPage = 1,
        pageCount = { relatedMaterials.size })

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
            relatedCreatures = relatedCreatures,
            relatedOreDeposits = relatedOreDeposits,
            relatedMaterials= relatedMaterials
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
    relatedCreatures: List<Creature>,
    relatedOreDeposits: List<OreDeposit>,
    relatedMaterials: List<Material>,
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
                    biome = biome,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    errorPainter = errorPainter
                )
                DetailExpandableText(text = biome.description.toString())
                mainBoss?.let { mainBoss ->
                    MainBossImageSection(mainBoss = mainBoss, errorPainter = errorPainter)
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
                        ContentScale.None,
                    )
                }
            }
        }
    )
}

@Composable
fun SlavicDivider() {
    val desiredHeight = 16.dp
    Box(
        modifier = Modifier.padding(vertical = BODY_CONTENT_PADDING.dp)
            .fillMaxWidth()
            .height(desiredHeight),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f).rotate(180f)) {
                StraitWhiteLine()
            }
            Image(
                painter = painterResource(id = R.drawable.divider_image),
                contentDescription = "Divider Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(desiredHeight) // Adjust height as needed
            )
            Box(modifier = Modifier.weight(1f)) {
                StraitWhiteLine()
            }
        }
    }
}


@Composable
fun StraitWhiteLine() {
    Canvas(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        val path = Path()
        val startPoint = Offset(0f, size.height / 2)
        val endPoint = Offset(size.width, size.height / 2)


        val gradientBrush = Brush.linearGradient(
            colors = listOf(Color.White, Color.White.copy(alpha = 0f)),
            start = startPoint,
            end = endPoint
        )

        drawLine(
            brush = gradientBrush,
            start = startPoint,
            end = endPoint,
            strokeWidth = 2.dp.toPx()
        )
    }
}


@Composable
fun HorizontalPagerSection(
    pagerState: PagerState,
    list: List<ItemData>,
    icon: ImageVector,
    title:String,
    subTitle:String,
    contentScale: ContentScale
) {
    val pageWidth = 160.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = (screenWidth - pageWidth) / 2
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
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
                        modifier = Modifier.rotate(-25f)
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Text(
                        title,
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
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                pageSize = PageSize.Fixed(pageWidth),
                beyondViewportPageCount = list.size,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    pagerSnapDistance = PagerSnapDistance.atMost(list.size)
                )
            ) { pageIndex ->
                Card(
                    Modifier
                        .size(150.dp)
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
                        }.shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(8.dp),
                            spotColor = Color.Black.copy(alpha = 0.25f)
                        )
                ) {
                    list.let {
                        Box(
                            modifier = Modifier
                                .height(150.dp),

                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxSize(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it[pageIndex].imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.item_grid_image),
                                contentScale = contentScale
                            )
                            Surface(
                                modifier = Modifier.size(18.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = ForestGreen10Dark,
                            ) {
                                Text(
                                    text = "${pageIndex+1}",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Surface(
                                modifier = Modifier.align(Alignment.BottomStart)
                                    .fillMaxHeight(0.2f)
                                    .fillMaxWidth(),
                                tonalElevation = 0.dp,
                                color = Color.Black.copy(alpha = ContentAlpha.medium),
                            ) {

                                Text(
                                    modifier = Modifier
                                        .padding
                                            (horizontal = 5.dp)
                                        .wrapContentHeight(align = Alignment.CenterVertically),
                                    text = it[pageIndex].name.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun RowTwoTridentDividers() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BODY_CONTENT_PADDING.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TridentDivider(
            modifier = Modifier
                .weight(1f)
                .rotate(180f)
        )
        Spacer(Modifier.weight(1f))
        TridentDivider(modifier = Modifier.weight(1f))
    }
}



@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainDetailImage(
    onBack: () -> Unit = {},
    biome: Biome,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    errorPainter: Painter? = null,
) {
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .heightIn(min = 200.dp, max = 320.dp),
            contentAlignment = Alignment.BottomStart,
        ) {
            AsyncImage(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "image-${biome.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 600)
                        }
                    )
                    .fillMaxSize()
                    .clickable {
                        onBack()
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(biome.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.item_grid_image),
                error = errorPainter,
                contentScale = ContentScale.Crop,
            )
            Surface(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "Surface-${biome.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 600)
                        }
                    )
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth(),
                tonalElevation = 0.dp,
                color = Color.Black.copy(alpha = ContentAlpha.medium),
            ) {

                Text(
                    modifier = Modifier
                        .padding
                            (horizontal = 8.dp)
                        .sharedElement(
                            state = rememberSharedContentState(key = "text-${biome.name}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 600)
                            }
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    text = biome.name,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall,
                )

            }
        }
    }
}


@Composable
fun DetailExpandableText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    text: String,
    collapsedMaxLine: Int = DEFAULT_MINIMUM_TEXT_LINE,
    showMoreText: String = "... show more",
    showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500, color = Color(0xFFAABBDD)),
    showLessText: String = " show less",
    showLessStyle: SpanStyle = showMoreStyle,
    textAlign: TextAlign? = null,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .padding(BODY_CONTENT_PADDING.dp)
            .clickable(clickable) {
                isExpanded = !isExpanded
            }
            .then(modifier)
    ) {
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        withStyle(style = showLessStyle) { append(showLessText) }
                    } else {
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
                        withStyle(style = showMoreStyle) { append(showMoreText) }
                    }
                } else {
                    append(text)
                }
            },

            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = textAlign,
        )
    }
}

@Composable
fun MainBossImageSection(
    modifier: Modifier = Modifier,
    mainBoss: MainBoss,
    errorPainter: Painter? = null,
) {
    Box(
        modifier = modifier
            .padding(BODY_CONTENT_PADDING.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(DETAIL_ITEM_SHAPE_PADDING))
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.Black.copy(alpha = 0.25f)
            ),
        contentAlignment = Alignment.TopStart
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(mainBoss.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_placeholder),
            error = errorPainter,
            contentDescription = stringResource(R.string.mainbosssection),
            contentScale = ContentScale.Crop,
        )
        Surface(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth(),
            tonalElevation = 0.dp,
            color = Color.Black.copy(alpha = ContentAlpha.medium),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mainBoss.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.Center)
                        .padding
                            (horizontal = 8.dp),
                )
                HorizontalDivider(
                    modifier = Modifier.width(100.dp),
                    thickness = 1.dp,
                    color = Color.White
                )
                Text(
                    text = "BOSS",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.Center)
                        .padding
                            (horizontal = 8.dp),
                )
            }

        }
    }
}

@Preview("DetailExpandableText", showBackground = true)
@Composable
fun PreviewDetailExpandableText() {
    ValheimVikiAppTheme {
        DetailExpandableText(text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum")
    }
}

@Composable
@Preview("RectangleSectionHeader")
fun PreviewRectangleSectionHeader() {
    ValheimVikiAppTheme {
        val pagerState = rememberPagerState(pageCount = {
            10
        })
        val creatureList = FakeData.generateFakeCreatures()
        HorizontalPagerSection(
            pagerState = pagerState,
            list = creatureList
            , Lucide.PawPrint,
            "Creatuers",
            "Creatures you may encounter in this biome",
            ContentScale.Crop,
        )
    }
}

@Preview("RowTwoTridentDividers", showBackground = true)
@Composable
fun PreviewRowTwoTridentDividers() {
    ValheimVikiAppTheme {
        RowTwoTridentDividers()
    }
}

// Preview przy użyciu statycznych danych
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview("BiomeImageHeaderSection 50.dp", showBackground = false)
@Composable
fun PreviewBiomeImageHeaderSection50() {

    val fakeMainBoss = MainBoss(
        id = "boss1",
        imageUrl = "https://via.placeholder.com/400x200.png?text=MainBoss+Image",
        category = "CREATURE",
        subCategory = "BOSS",
        name = "BoneMass",
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
    ValheimVikiAppTheme {
        MainBossImageSection(
            errorPainter = painterResource(R.drawable.preview_image),
            mainBoss = fakeMainBoss,
        )
    }
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
                    relatedMaterials = materials
                )
            }
        }
    }
}

