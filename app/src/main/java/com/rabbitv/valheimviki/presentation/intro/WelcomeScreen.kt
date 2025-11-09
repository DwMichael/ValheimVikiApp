package com.rabbitv.valheimviki.presentation.intro


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.welcome.OnBoardingPage
import com.rabbitv.valheimviki.navigation.GridDestination
import com.rabbitv.valheimviki.presentation.intro.viewmodel.WelcomeViewModel
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.Constants.LAST_ON_BOARDING_PAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun WelcomeScreen(
	navController: NavHostController,
	welcomeViewModel: WelcomeViewModel = hiltViewModel()
) {
	val pages = listOf(
		OnBoardingPage.First,
		OnBoardingPage.Second,
		OnBoardingPage.Third
	)
	val pagerState = rememberPagerState { pages.size }
	val currentPage = pagerState.currentPage
	val horizontalPadding = 16.dp
	val scope = rememberCoroutineScope()

	Column(
		modifier = Modifier
			.testTag("WelcomeScreen")
			.fillMaxSize()
			.paint(
				painterResource(id = pages[currentPage].image),
				contentScale = ContentScale.Crop
			)
			.padding(bottom = 50.dp)
	) {
		HorizontalPager(
			state = pagerState,
			verticalAlignment = Alignment.Top,
			modifier = Modifier
				.weight(7f)
				.fillMaxWidth()
		) { position ->

			PagerScreen(onBoardingPage = pages[position], position, horizontalPadding)
		}
		NavigationButton(
			pages[currentPage],
			horizontalPadding,
			pagerState,
			scope,
			navController,
			welcomeViewModel
		)
		Row(
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			repeat(pages.size) { page ->
				Box(
					modifier = Modifier
						.width(12.dp)
						.height(12.dp)
						.clip(CircleShape)
						.background(
							if (page == pagerState.currentPage)
								ForestGreen10Dark
							else
								MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
						)
				)
				if (page < pages.size - 1) {
					Spacer(Modifier.width(8.dp))
				}
			}
		}
	}
}


@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage, position: Int, horizontalPadding: Dp) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = horizontalPadding),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (position == 0) {
			Image(
				painter = painterResource(id = R.drawable.valheim_viki_log_no_bg),
				contentDescription = "VikingLogo",
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.size(260.dp)
					.clip(CircleShape)
			)
			Spacer(modifier = Modifier.height(24.dp))
		}
		Text(
			text = onBoardingPage.description,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.labelLarge,
			color = Color.White
		)
	}
}

@Composable
fun NavigationButton(
	onBoardingPage: OnBoardingPage,
	horizontalPadding: Dp,
	pagerState: PagerState,
	scope: CoroutineScope,
	navController: NavHostController,
	welcomeViewModel: WelcomeViewModel
) {
	ElevatedButton(
		onClick = {
			scope.launch {
				if (pagerState.currentPage != LAST_ON_BOARDING_PAGE) {
					pagerState.scrollToPage(pagerState.currentPage + 1)
				} else {
					welcomeViewModel.saveOnBoardingState(completed = true)
					navController.popBackStack()
					navController.navigate(GridDestination.WorldDestinations.BiomeGrid)
				}
			}
		},
		modifier = Modifier
			.height(64.dp)
			.fillMaxWidth()
			.padding(horizontal = horizontalPadding),
		shape = RoundedCornerShape(12.dp),
		colors = ButtonDefaults.elevatedButtonColors(
			containerColor = ForestGreen10Dark,
			contentColor = Color.White,
			disabledContainerColor = ForestGreen10Dark.copy(alpha = 0.5f),
			disabledContentColor = Color.White.copy(alpha = 0.5f)
		),
		contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 8.dp),
		interactionSource = remember { MutableInteractionSource() }
	) {
		Text(
			text = onBoardingPage.buttonTitle,
			style = TextStyle(
				color = Color.White,
				fontSize = 16.sp,
				fontWeight = FontWeight(600),
				letterSpacing = 0.6.sp,
				lineHeight = 22.sp,
				textAlign = TextAlign.Center
			)
		)
	}
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun FirstOnBoardingScreenPreview() {
	ValheimVikiAppTheme {
		Column(modifier = Modifier.fillMaxSize()) {
			PagerScreen(onBoardingPage = OnBoardingPage.First, 0, 16.dp)
		}
	}
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun SecondOnBoardingScreenPreview() {
	ValheimVikiAppTheme {
		Column(modifier = Modifier.fillMaxSize()) {
			PagerScreen(onBoardingPage = OnBoardingPage.Second, 1, 16.dp)
		}
	}
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun ThirdOnBoardingScreenPreview() {
	ValheimVikiAppTheme {
		Column(modifier = Modifier.fillMaxSize()) {
			PagerScreen(onBoardingPage = OnBoardingPage.Third, 2, 16.dp)
		}
	}
}


@Preview(name = "WelcomePage")
@Composable
private fun PreviewWelcomePage() {
	ValheimVikiAppTheme {
		val navController = rememberNavController()
		WelcomeScreen(navController = navController)
	}
}