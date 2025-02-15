package com.rabbitv.valheimviki.domain.model.welcome

import androidx.annotation.DrawableRes
import com.rabbitv.valheimviki.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String,
    val buttonTitle: String,
) {
    object First : OnBoardingPage(
        image = R.drawable.welcome_bg,
        title = "ValheimViki",
        description = "Explore Valheim's vast world & knowledge",
        buttonTitle = "GET STARTED",
    )

    object Second : OnBoardingPage(
        image = R.drawable.welcome_bg_second,
        title = "Explore",
        description = "",
        buttonTitle = "CONTINUE"
    )

    object Third : OnBoardingPage(
        image = R.drawable.welcome_bg_third,
        title = "Welcome",
        description = "Thank you for downloading ValheimViki.\n" +
                " Dive into the world of Valheim and explore the rich knowledge base we have to " +
                "offer.",
        buttonTitle = "EXPLORE NOW"
    )
}