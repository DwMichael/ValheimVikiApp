package com.rabbitv.valheimviki.presentation.detail.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.detail.food.model.FoodDetailUiState
import com.rabbitv.valheimviki.presentation.detail.food.viewmodel.FoodDetailViewModel
import com.rabbitv.valheimviki.ui.theme.Shapes

@Composable
fun FoodDetailScreen(
	onBack: () -> Unit,
	viewModel: FoodDetailViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	FoodDetailContent(
		uiState = uiState,
		onBack = onBack
	)

}


@Composable
fun FoodDetailContent(
	uiState: FoodDetailUiState,
	onBack: () -> Unit
) {
	Scaffold(
	) { innerPadding ->
		Box(
			modifier = Modifier.fillMaxSize()
		) {
			Image(
				painter = painterResource(R.drawable.main_background),
				contentDescription = "bg",
				contentScale = ContentScale.FillBounds
			)
		}
		Column(
			modifier = Modifier.padding(innerPadding)
		) {
			Image(
				painter = painterResource(R.drawable.bg_food),
				contentDescription = "bg",
				contentScale = ContentScale.FillBounds,
				modifier = Modifier
					.clip(Shapes.large)
					.size(150.dp)
			)
		}

	}


}