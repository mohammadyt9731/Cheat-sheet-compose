package com.example.cheatsheet.ui.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ListItemModel(
    val text: String,
    var color: @RawValue Color = Color.Black,
):Parcelable
