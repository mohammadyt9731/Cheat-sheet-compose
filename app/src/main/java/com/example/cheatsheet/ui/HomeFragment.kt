package com.example.cheatsheet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import com.example.chaetsheet.R
import com.example.cheatsheet.ui.theme.CheatSheetTheme

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                CheatSheetTheme {
                    Text(text = "Mohammad")
                }
            }
        }
    }
}