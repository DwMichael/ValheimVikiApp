package com.rabbitv.valheimviki.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.rabbitv.valheimviki.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val IMFellEnglish = GoogleFont("IM Fell English SC")
val roboto = GoogleFont("Roboto")

val IMFellEnglishFontFamily = FontFamily(
    Font(googleFont = IMFellEnglish, fontProvider = provider)
)

val robotoFontFamily = FontFamily(
    Font(googleFont = roboto, fontProvider = provider)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = IMFellEnglishFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    titleLarge = TextStyle(
        fontFamily = IMFellEnglishFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 45.sp,
        letterSpacing = 0.15.sp
    ),


    labelSmall = TextStyle(
        fontFamily =robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)