package com.example.cheatsheet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primaryVariant = Purple700,
    secondary = Teal200,

    primary = Blue700,
    onPrimary = White600,
    background = White700,
    onBackground = SemiBlack,
    surface = Blue200,
    onSurface = Blue700
)


private val LightColorPalette = lightColors(
    primaryVariant = Purple700,
    secondary = Teal200,

    primary = Blue700,
    onPrimary = White600,
    background = White700,
    onBackground = SemiBlack,
    surface = Blue200,
    onSurface = Blue700
)

@Composable
fun CheatSheetTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}