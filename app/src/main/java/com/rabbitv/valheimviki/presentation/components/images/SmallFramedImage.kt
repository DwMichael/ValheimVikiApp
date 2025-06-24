package com.rabbitv.valheimviki.presentation.components.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.ui.theme.BlackBrownBorder
import com.rabbitv.valheimviki.ui.theme.Shapes
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun SmallFramedImage(imageUrl: String) {
	Box(
		Modifier
			.size(150.dp)
			.border(1.dp, BlackBrownBorder, Shapes.large)
	)
	{
		Image(
			painter = painterResource(R.drawable.bg_food),
			contentDescription = "bg",
			contentScale = ContentScale.FillBounds,
			modifier = Modifier
				.clip(Shapes.large)
				.fillMaxSize()
		)
		AsyncImage(
			modifier = Modifier
				.fillMaxSize(0.7f)
				.align(Alignment.Center),
			model = ImageRequest.Builder(LocalContext.current)
				.data(imageUrl)
				.crossfade(true)
				.build(),
			contentDescription = "Food Image",
			error = if (LocalInspectionMode.current) painterResource(R.drawable.testweapon) else null,
			contentScale = ContentScale.FillBounds,
		)

	}
}

@Preview("FoodImage")
@Composable
fun PreviewFoodImage() {
	ValheimVikiAppTheme {
		SmallFramedImage("")
	}

}