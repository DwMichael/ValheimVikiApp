package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
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
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.TridentDivider
import com.rabbitv.valheimviki.ui.theme.DETAIL_ITEM_SHAPE_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
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
    val pagerState = rememberPagerState(pageCount = {
        relatedCreatures.size
    })

    biome?.let { biome ->
        BiomeDetailContent(
            biome = biome,
            mainBoss = mainBoss,
            onBack = onBack,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            pagerState = pagerState,
            relatedCreatures = relatedCreatures,
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
    relatedCreatures: List<Creature>,
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
                DetailImage(
                    onBack = onBack,
                    biome = biome,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    errorPainter = errorPainter
                )
                DetailExpandableText(text = biome.description.toString())
                mainBoss?.let { mainBoss ->
                    ImageHeaderSection(mainBoss = mainBoss, errorPainter = errorPainter)
                }
                RowTwoTridentDividers()
                CreatureHorizontalPagerSection(pagerState, relatedCreatures)
            }
        }
    )
}

@Composable
fun CreatureHorizontalPagerSection(
    pagerState: PagerState,
    creaturesList: List<Creature>
) {
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
                        painter = painterResource(id = R.drawable.pets_24),
                        tint = Color.White,
                        contentDescription = "Rectangle section Icon",
                        modifier = Modifier.rotate(-25f)
                    )
                    Spacer(modifier = Modifier.width(11.dp))
                    Text(
                        "Creatures",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp))
                Text(
                    "Creatures you may encounter in this biome",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Spacer(modifier = Modifier.padding(6.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 0.dp),
                pageSize = PageSize.Fixed(160.dp)
            ) { pageIndex ->
                Card(
                    Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            val pageOffset = (
                                    (pagerState.currentPage - pageIndex) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue

                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    creaturesList.let {
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
                                contentScale = ContentScale.Crop,
                            )
                            Surface(
                                modifier = Modifier.size(18.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = ForestGreen10Dark,
                            ) {
                                Text(
                                    text = "$pageIndex",
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
fun DetailImage(
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
            .padding(
                start = BODY_CONTENT_PADDING.dp,
                end = BODY_CONTENT_PADDING.dp,
                bottom = BODY_CONTENT_PADDING.dp,
                top = BODY_CONTENT_PADDING.dp
            )
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
fun ImageHeaderSection(
    modifier: Modifier = Modifier,
    mainBoss: MainBoss,
    errorPainter: Painter? = null,
) {
    Box(
        modifier = modifier
            .height(250.dp)
            .clip(RoundedCornerShape(DETAIL_ITEM_SHAPE_PADDING))
            .padding(
                start = BODY_CONTENT_PADDING.dp,
                end = BODY_CONTENT_PADDING.dp,
                bottom = BODY_CONTENT_PADDING.dp,
                top = BODY_CONTENT_TOP_PADDING.dp
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
        val creatureList = generateFakeCreatures()
        CreatureHorizontalPagerSection(
            pagerState = pagerState,
            creaturesList = creatureList
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
@Preview("BiomeImageHeaderSection 50.dp", showBackground = true)
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
        ImageHeaderSection(
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
    val creatureList = generateFakeCreatures()
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
                    relatedCreatures = creatureList
                )
            }
        }
    }
}

fun generateFakeCreatures(): List<Creature> {
    return listOf(
        Creature(
            id = "creature001",
            category = "Boss",
            subCategory = "Elder",
            imageUrl = "https://example.com/meadows_troll.png",
            name = "Meadows Troll",
            description = "A massive troll that roams the peaceful meadows. Despite its size, it's known for its gentle nature unless provoked.",
            order = 1,
            levels = 3,
            baseHP = 1000,
            weakness = "Fire",
            resistance = "Frost",
            baseDamage = "100-150",
            collapseImmune = "Yes",
            forsakenPower = "Nature's Wrath",
            imageStarOne = "https://example.com/meadows_troll_star1.png",
            imageStarTwo = "https://example.com/meadows_troll_star2.png"
        ),
        Creature(
            id = "creature002",
            category = "Aggressive",
            subCategory = "Undead",
            imageUrl = "https://example.com/frost_draugr.png",
            name = "Frost Draugr",
            description = "An ancient warrior risen from the dead in the frozen mountains. Carries ice-encrusted weapons and armor.",
            order = 2,
            levels = 2,
            baseHP = 150,
            weakness = "Fire, Spirit",
            resistance = "Frost, Poison",
            baseDamage = "35-45",
            imageStarOne = "https://example.com/frost_draugr_star1.png",
            imageStarTwo = "https://example.com/frost_draugr_star2.png"
        ),
        Creature(
            id = "creature003",
            category = "Passive",
            subCategory = "Wildlife",
            imageUrl = "https://example.com/glowing_deer.png",
            name = "Glowing Deer",
            description = "A magical deer with bioluminescent antlers that light up the Black Forest at night. Its hide has alchemical properties.",
            order = 3,
            levels = 1,
            baseHP = 60,
            weakness = "Pierce",
            resistance = "None",
            baseDamage = "5-10",
            abilities = "Night Vision, Swift Movement",
            imageStarOne = null,
            imageStarTwo = null
        ),
        Creature(
            id = "creature004",
            category = "MiniBoss",
            subCategory = "Elemental",
            imageUrl = "https://example.com/swamp_guardian.png",
            name = "Swamp Guardian",
            description = "A sentient mass of vines, mud, and ancient bones that protects the heart of the swamp. Can summon lesser creatures to its aid.",
            order = 4,
            levels = 2,
            baseHP = 500,
            weakness = "Fire, Slash",
            resistance = "Blunt, Pierce, Poison",
            baseDamage = "65-80",
            collapseImmune = "Yes",
            forsakenPower = null,
            imageStarOne = "https://example.com/swamp_guardian_star1.png",
            imageStarTwo = "https://example.com/swamp_guardian_star2.png"
        ),
        Creature(
            id = "creature005",
            category = "Aggressive",
            subCategory = "Insect",
            imageUrl = "https://example.com/plains_mantis.png",
            name = "Plains Mantis",
            description = "A gigantic praying mantis that camouflages in the tall grass of the plains. Known for its lightning-quick strikes and precise hunting.",
            order = 5,
            levels = 3,
            baseHP = 220,
            weakness = "Blunt, Frost",
            resistance = "Pierce, Slash",
            baseDamage = "75-85",
            imageStarOne = "https://example.com/plains_mantis_star1.png",
            imageStarTwo = "https://example.com/plains_mantis_star2.png"
        )
    )
}