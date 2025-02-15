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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.welcome.OnBoardingPage
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.IMFellEnglishFontFamily
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier
        .testTag("WelcomeScreen")
        .fillMaxSize()
) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )
    val pagerState = rememberPagerState { pages.size }
    val currentPage = pagerState.currentPage


    Column(
        modifier = Modifier
            .fillMaxSize().paint(
                painterResource(id = pages[currentPage].image),
                contentScale = ContentScale.Crop
            )
    ) {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .weight(7f)
                .fillMaxWidth()
        ) { position ->

            PagerScreen(onBoardingPage = pages[position],position)
        }
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
fun PagerScreen(onBoardingPage: OnBoardingPage,position:Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(position==0) {
            Image(
                painter = painterResource(id = R.drawable.viking_logo_hd),
                contentDescription = "VikingLogo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
            )
        Spacer(modifier = Modifier.height(24.dp))
        }
        Text(
            modifier = Modifier.testTag("AppTitle"),
            text = onBoardingPage.title,
            style = TextStyle(
                color = Color.White,
                fontSize = 48.sp,
                fontFamily = IMFellEnglishFontFamily,
                fontWeight = FontWeight(800),
                letterSpacing = 0.15.sp,
                lineHeight = 62.sp,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .testTag("AppTitle")
                .height(44.dp),
        ) {
            Text(

                text = onBoardingPage.description,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = IMFellEnglishFontFamily,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(

            {
                println("Button Clicked!")
            },
            Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            true,
            shape = RoundedCornerShape(12.dp),
            ButtonDefaults.elevatedButtonColors(
                containerColor = ForestGreen10Dark,
                contentColor = Color.White,
                disabledContainerColor = ForestGreen10Dark.copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            ButtonDefaults.buttonElevation(),
            null,
            PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            remember { MutableInteractionSource() }
        ) {
            Text(
                text = onBoardingPage.buttonTitle,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = IMFellEnglishFontFamily,
                    fontWeight = FontWeight(600),
                    letterSpacing = 0.6.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )

            )
        }
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun FirstOnBoardingScreenPreview() {
    ValheimVikiAppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.First,0)
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun SecondOnBoardingScreenPreview() {
    ValheimVikiAppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.Second,1)
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun ThirdOnBoardingScreenPreview() {
    ValheimVikiAppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            PagerScreen(onBoardingPage = OnBoardingPage.Third,2)
        }
    }
}


@Preview(name = "WelcomePage")
@Composable
private fun PreviewWelcomePage() {
    ValheimVikiAppTheme {
        WelcomeScreen()
    }
}