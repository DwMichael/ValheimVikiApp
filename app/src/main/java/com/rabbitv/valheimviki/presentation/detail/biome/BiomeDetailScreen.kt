package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.placeholder
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@Composable
fun BiomeDetailScreen(
    viewModel: BiomeDetailScreenViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val biome by viewModel.biome.collectAsStateWithLifecycle()
    val sizeResolver = rememberConstraintsSizeResolver()
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalPlatformContext.current)
            .data(biome?.imageUrl.toString())
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .size(sizeResolver)
            .build(),
    )
            Column(
                modifier = Modifier.testTag("BiomeDetailScreen")
                    .fillMaxWidth()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
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
                            text = biome?.name ?: "",
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
                    text = biome?.description.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding
                            (horizontal = 8.dp),
                )

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

    ValheimVikiAppTheme{

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