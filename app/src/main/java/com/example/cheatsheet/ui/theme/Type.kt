package com.example.cheatsheet.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.chaetsheet.R

val Typography = Typography(

    h5 = TextStyle(
        fontFamily = FontFamily(Font(R.font.outfit_semibold)),
        fontSize = 24.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.outfit_regular)),
        fontSize = 12.sp
    ),
    button = TextStyle(
        fontFamily = FontFamily(Font(R.font.outfit_semibold)),
        fontSize = 12.sp
    )
)