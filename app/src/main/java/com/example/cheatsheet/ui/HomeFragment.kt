package com.example.cheatsheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.chaetsheet.R
import com.example.cheatsheet.ui.theme.CheatSheetTheme

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreen()
            }
        }
    }

    @Composable
    fun HomeScreen() {
        CheatSheetTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Greeting(stringResource(id = R.string.label_myFullName))
                    Spacer(modifier = Modifier.height(20.dp))
                    TopImage(
                        painter = painterResource(id = R.drawable.mountain),
                        title = stringResource(id = R.string.label_mountain)
                    )
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onBackground
                    )
                ) {
                    append(stringResource(id = R.string.label_hi))
                }
                append(" $name!")
            },
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )
    }

    @Composable
    fun TopImage(painter: Painter, title: String) {

        Card(
            Modifier
                .fillMaxWidth()
                .height(208.dp),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary)
        ) {

            Image(
                painter = painter,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.White
                )
            }
        }
    }
}