package com.rabbitv.valheimviki.presentation.components.main_detail_image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.generateFakeMaterials

@Composable
fun MainDetailImage(
	imageUrl: String,
	name: String,
	textAlign: TextAlign = TextAlign.Start,
	imageScale: Float = 1.0f,
	contentScale: ContentScale = ContentScale.Crop,
	backgroundImageColor: Color = Color.Transparent
) {
	Box(
		modifier = Modifier
            .background(backgroundImageColor)
            .height(320.dp),
	) {
		AsyncImage(
			modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(fraction = imageScale),
			model = ImageRequest.Builder(LocalContext.current)
				.data(imageUrl)
				.crossfade(true)
				.build(),
			contentDescription = stringResource(R.string.item_grid_image),
			error = if (LocalInspectionMode.current) painterResource(R.drawable.testweapon) else null,
			contentScale = contentScale,
		)
		Surface(
			modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(0.25f)
                .fillMaxWidth(),
			tonalElevation = 0.dp,
			color = Color.Black.copy(alpha = ContentAlpha.medium),
		) {
			Text(
				modifier = Modifier
                    .padding
                        (horizontal = 8.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
				textAlign = textAlign,
				text = name,
				color = Color.White,
				style = MaterialTheme.typography.displaySmall,
			)

		}
	}
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewMainDetailImage() {
	ValheimVikiAppTheme {
		generateFakeMaterials()
		MainDetailImage(
			imageUrl = "ss",
			name = "Boar"
		)
	}
}
