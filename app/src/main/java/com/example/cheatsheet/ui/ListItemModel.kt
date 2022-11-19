package com.example.cheatsheet.ui

import androidx.compose.ui.graphics.Color

data class ListItemModel(
    val text: String,
    var color: Color = Color.Black,
    var isShowDivider: Boolean = true
)
