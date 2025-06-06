package com.rabbitv.valheimviki.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = ForestGreen30Dark,
    primary = PrimaryWhite,
    onPrimary = PrimaryWhite,
    onPrimaryContainer = PrimaryOrange,
    error = Color.Red,
    secondary = PrimaryOrange,
    tertiary = PrimaryGrey,

    )

private val LightColorScheme = lightColorScheme(
    background = ForestGreen30Dark,
    primary = ForestGreen30Dark,
    secondary = PrimaryOrange,
    tertiary = PrimaryGrey
)

@Composable
fun ValheimVikiAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}