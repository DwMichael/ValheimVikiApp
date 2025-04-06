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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

const val DEFAULT_MINIMUM_TEXT_LINE = 4
const val BODY_CONTENT_PADDING = 10

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BiomeDetailScreen(
    onBack : () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: BiomeDetailScreenViewModel = hiltViewModel(),
    paddingValues: PaddingValues,

    ) {
    val biome by viewModel.biome.collectAsStateWithLifecycle()

    biome?.let { biome ->
        Scaffold(
            content = {
                Column(
                    modifier = Modifier
                        .testTag("BiomeDetailScreen")
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    DetailImage(
                        onBack = onBack,
                        animatedVisibilityScope = animatedVisibilityScope,
                        biome = biome,
                        sharedTransitionScope = sharedTransitionScope
                    )
                    DetailExpandableText(
                        text = biome.description.toString(),
                    )

                }
            }
        )
    }

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailImage(
    onBack : () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    biome: Biome,
) {
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .heightIn(min = 200.dp, max = 320.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            AsyncImage(
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(key = "image-${biome.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 10000)
                    }
                ).clickable{
                    onBack()
                }
                ,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(biome.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_placeholder),
                contentDescription = stringResource(R.string.item_grid_image),
                contentScale = ContentScale.Crop,
            )
            Surface(
                modifier = Modifier
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth(),
                tonalElevation = 0.dp,
                color = Color.Black.copy(alpha = ContentAlpha.medium),
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding
                            (horizontal = 8.dp)
                        .sharedElement(
                            state = rememberSharedContentState(key = "text-${biome.name}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 10000)
                            }
                        ),
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
    var lastCharIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
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

//
//@Composable
//fun ImageHeaderSection(bossId:String){
//
//
//    Box(
//        modifier = Modifier
//            .heightIn(min = 200.dp, max = 320.dp),
//        contentAlignment = Alignment.BottomStart
//    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(imageUrl)
//                .crossfade(true)
//                .build(),
//            placeholder = painterResource(R.drawable.ic_placeholder),
//            contentDescription = stringResource(R.string.item_grid_image),
//            contentScale = ContentScale.Crop,
//        )
//        Surface(
//            modifier = Modifier
//                .fillMaxHeight(0.2f)
//                .fillMaxWidth(),
//            tonalElevation = 0.dp,
//            color = Color.Black.copy(alpha = ContentAlpha.medium),
//        ) {
//            Text(
//                text = nameOfItem,
//                color = Color.White,
//                style = MaterialTheme.typography.displaySmall,
//                modifier = Modifier
//                    .wrapContentHeight(align = Alignment.CenterVertically)
//                    .padding
//                        (horizontal = 8.dp),
//            )
//        }
//    }
//}


@Preview(name = "DetailImage", showBackground = true)
@Composable
fun PreviewDetailExpandableText() {
    DetailExpandableText(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed rutrum vel quam id luctus. Aenean leo ex, pharetra quis consequat ac, luctus vel leo. Curabitur a justo id arcu eleifend vehicula. Sed odio leo, tempus id metus sit amet, laoreet auctor nunc. Etiam sagittis euismod pretium. Nunc et molestie elit, non fermentum nisl. Mauris quis massa quis dolor viverra ultricies et sit amet risus. Proin ac elit sed turpis mattis varius. Pellentesque tincidunt ligula in ante ornare, vel ullamcorper risus volutpat")
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(name = "DetailImage", showBackground = true)
@Composable
private fun PreviewDetailImage() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            DetailImage(
                animatedVisibilityScope = this,
                biome = Biome(
                    id = "SSS",
                    category = "SSSSS",
                    imageUrl = "https://s3.eu-central-1.amazonaws.com/cdn.psy.pl-migration/down_syndrome_in_dogs_e1621852707104_73c6b22e93.jpg",
                    name = "MEADOWS",
                    description = "MEADOWS",
                    order = 1
                ),
                onBack = {},
                sharedTransitionScope = this@SharedTransitionLayout
            )
        }
    }
}


@Preview(name = "BiomeDetail", showBackground = true)
@Composable
private fun PreviewBiomeDetail() {
    val biome = Biome(
        id = "0ffe7174-a550-4f9c-88c0-1e768be323a4",
        imageUrl = "https://s3.eu-central-1.amazonaws.com/cdn.psy.pl-migration/down_syndrome_in_dogs_e1621852707104_73c6b22e93.jpg",
        category = "BIOME",
        name = "Mountain",
        description = "The Mountain biome features frozen, snow‐covered peaks framed by fir trees. This unforgiving region is home to wolves, drakes, and stone golems. Frost caves – dark dungeons inhabited by cultists, bats, and Ulv – are also found here. The extreme cold inflicts a freezing effect on players, which must be countered with frost resistance mead, appropriate capes, or nearby campfires.",
        order = 5
    )

    ValheimVikiAppTheme {

        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalPlatformContext.current)
                .data(biome.imageUrl.toString())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .build(),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            )
            {
                Box(
                    modifier = Modifier
                        .height(235.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Surface(
                        color = Color.Transparent,
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = stringResource(R.string.item_grid_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()

                        )
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxHeight(0.2f)
                            .fillMaxWidth(),
                        tonalElevation = 0.dp,
                        color = Color.Black.copy(alpha = ContentAlpha.medium),
                    ) {
                        Text(
                            text = biome.name,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically)
                                .padding
                                    (horizontal = 8.dp),
                        )
                    }
                }

                Text(
                    text = biome.name.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding
                            (horizontal = 8.dp),
                )
                Text(
                    text = biome.imageUrl.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding
                            (horizontal = 8.dp),
                )
                Text(
                    text = biome.description.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding
                            (horizontal = 8.dp),
                )
            }
        }

    }

}