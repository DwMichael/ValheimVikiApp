package com.rabbitv.valheimviki.presentation.detail.creature

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.Flame
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
import com.rabbitv.valheimviki.presentation.components.HorizontalPagerSection
import com.rabbitv.valheimviki.presentation.components.ImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.trident_dividers.TridentsDividedRow
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DETAIL_ITEM_SHAPE_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkGrey
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark

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
            sharedTransitionScope = sharedTransitionScope,
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
    relatedSummoningItems:List<Material?>,
    dropItems : List<ItemData>,
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
                    errorPainter = errorPainter,
                    textAlign = TextAlign.Center
                )
                DetailExpandableText(text = mainBoss.description.toString())
                TridentsDividedRow(text = "BOSS DETAIL")
                relatedForsakenAltar?.let {
                    ImageWithTopLabel(
                        itemData = relatedForsakenAltar,
                        horizontalDividerWidth = 250.dp,
                        textStyle = MaterialTheme.typography.titleLarge
                    )
                }
                SlavicDivider()
                relatedSummoningItems.isNotEmpty().let {
                    CardWithOverlayLabel(
                        height =180.dp,
                        alpha = 0.1f,
                        painter = painterResource(R.drawable.summoning_bg),
                        content = {
                            Column {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OverlayLabel(
                                        icon = Lucide.TreePine,
                                        label = "PRIMARY SPAWN",
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
                relatedBiome?.let {
                    TridentsDividedRow()
                    CardWithOverlayLabel(
                        painter =  painter,
                        content = {
                            Row {
                                Box(
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    OverlayLabel(
                                        icon = Lucide.Flame,
                                        label = "SUMMONING ITEMS",
                                    )
                                }
                                Text(
                                    "${it.name.uppercase()}X${it.}",
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






                Box(modifier = Modifier.size(45.dp))
            }
        }
    )
}

@Composable
fun CustomRowLayout(
    relatedSummoningItems: List<Material?>,
    modifier: Modifier,
){

    LazyRow(
        modifier = modifier.padding(BODY_CONTENT_PADDING.dp).wrapContentHeight(Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(relatedSummoningItems.size) { index ->
            ItemCard(
                list = relatedSummoningItems,
                pageIndex = index,
                contentScale = ContentScale.Crop,
            )
        }
    }
}


@Composable
fun ItemCard(
    list: List<ItemData?>,
    pageIndex: Int,
    contentScale: ContentScale,
){
    Card(
        Modifier
            .size(100.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.White.copy(alpha = 0.25f)
            )
    ) {
        list.let {
            Box(
                modifier = Modifier.background(DarkGrey)
                    .height(150.dp),

                ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize().padding(BODY_CONTENT_PADDING.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it[pageIndex]?.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.item_grid_image),
                    contentScale = contentScale
                )
                Surface(
                    modifier = Modifier.height(18.dp).width(28.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(2.dp)),
                    color = ForestGreen10Dark,
                ) {
                    Text(
                        text = "${pageIndex+1}/${list.size}",
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
                        text = it[pageIndex]?.name.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
fun CardWithOverlayLabel(
    painter: Painter,
    height: Dp = 75.dp,
    content: @Composable () -> Unit = {},
    alpha: Float = 0.6f
) {
    Box(
        modifier = Modifier
            .padding(BODY_CONTENT_PADDING.dp)
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .paint(
                painter = painter,
                contentScale = ContentScale.Crop
            )
    ) {
        Card(
            modifier = Modifier
                .background(Color.Black.copy(alpha = alpha))
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent // <- Important
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            content()
        }
    }
}


@Composable
fun OverlayLabel(
    icon: ImageVector,
    label: String
) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    bottomEnd = DETAIL_ITEM_SHAPE_PADDING,
                    topStart = DETAIL_ITEM_SHAPE_PADDING
                )
            )
            .background(Color.Black.copy(alpha = 0.4f))
            .wrapContentHeight(Alignment.Top)
            .wrapContentWidth(Alignment.Start)

    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                modifier = Modifier.height(12.dp),
                tint = Color.White,
                contentDescription = "Icon Label",

                )
            Text(
                label,

                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color.White
            )
        }
    }
}

@Composable
fun CardWithOverlayLabelAndHorizontalPager(
    onBack: () -> Unit,
    text: String,
    backgroundImage: Painter,
) {

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