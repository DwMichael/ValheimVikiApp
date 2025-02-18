package com.rabbitv.valheimviki.domain.model.welcome

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
        buttonTitle = "GET STARTED"
    )

    object Second : OnBoardingPage(
        image = R.drawable.welcome_bg_second,
        title = "EXPLORE",
        description = "A beautiful, procedurally generated world awaits you, shrouded in mystery. Venture into deep dark forests, climb snow-peaked mountains and discover what’s left of those who came before you. Take to the seas on a mighty longship, but be wary of sailing too far…",
        buttonTitle = "CONTINUE"
    )

    object Third : OnBoardingPage(
        image = R.drawable.welcome_bg_third,
        title = "WELCOME",
        description = "Thank you for downloading ValheimViki.\n" +
                " Dive into the world of Valheim and explore the rich knowledge base we have to " +
                "offer.",
        buttonTitle = "EXPLORE NOW"
    )
}