package com.rabbitv.valheimviki.presentation.components.main_detail_image


import androidx.compose.animation.AnimatedVisibility

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark

import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.generateFakeMaterials
import kotlinx.coroutines.delay



@Composable
fun AsyncImageAnimated(
    onBack: () -> Unit = {},
    imageUrl: String,
    imageScale: Float = 1.0f,
    contentScale :ContentScale = ContentScale.Crop,
    backgroundImageColor: Color = Color.Transparent,
    height: Dp = 340.dp
) {
    val backButtonVisibleState = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(250)
        backButtonVisibleState.value = true
    }
    Box(
        modifier = Modifier
            .background(backgroundImageColor)
            .height(height)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize(fraction = imageScale)) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.item_grid_image),
                error = if (LocalInspectionMode.current) painterResource(R.drawable.testweapon) else null,
                contentScale = contentScale,
            )
        }
        AnimatedVisibility(
            visible = backButtonVisibleState.value,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            FilledIconButton(
                onClick = onBack,
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = ForestGreen10Dark,
                ),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAsyncImageAnimated() {
    ValheimVikiAppTheme {

                val itemsData = generateFakeMaterials()
                AsyncImageAnimated(
                    onBack = {},
                    imageUrl = itemsData.first().imageUrl,
                    )
            }
}