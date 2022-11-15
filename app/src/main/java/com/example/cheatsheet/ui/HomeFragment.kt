package com.example.cheatsheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.chaetsheet.R
import com.example.cheatsheet.ui.theme.CheatSheetTheme
import com.example.cheatsheet.ui.theme.Gray
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.chip.Chip
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

                    Greeter(stringResource(id = R.string.label_myFullName))
                    Spacer(modifier = Modifier.height(20.dp))
                    TopImage(
                        painter = painterResource(id = R.drawable.mountain),
                        title = stringResource(id = R.string.label_mountain)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ListSection()
                }
            }
        }
    }

    @Composable
    fun Greeter(name: String) {
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

    @Composable
    fun ListSection() {
        var inputValueState by remember {
            mutableStateOf("")
        }

        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


            InputField(value = inputValueState) {
                inputValueState = it
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RoundedButton(
                    text = stringResource(id = R.string.label_add),
                    modifier = Modifier
                        .height(42.dp)
                        .weight(1f),
                ) {

                }
                Spacer(modifier = Modifier.width(24.dp))
                RoundedButton(
                    text = stringResource(id = R.string.label_Reset),
                    modifier = Modifier
                        .height(42.dp)
                        .weight(1f),
                ) {
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            TabsAndPager()
        }
    }

    @Composable
    fun InputField(value: String, onValueChange: (String) -> Unit) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.label_typeSomeThing),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            }
        )
    }

    @Composable
    fun RoundedButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
        Button(
            modifier = modifier,
            onClick = onClick,
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {

            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.button
            )

        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun TabsAndPager() {
        val pagerState = rememberPagerState(pageCount = 2)
        val selectedIndex = pagerState.currentPage
        val coroutineScope = rememberCoroutineScope()
        val listItems = listOf(
            "Something1",
            "Something22",
            "Something333",
            "Something44",
            "Something",
            "Something5555",
            "Something5555"
        )

        val tabTitles = listOf(
            stringResource(id = R.string.label_list),
            stringResource(id = R.string.label_chip)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


            TabRow(
                selectedTabIndex = selectedIndex,
                Modifier
                    .fillMaxWidth()
                    .background(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colors.surface
                    ), backgroundColor = Color.Transparent,
                indicator = { },
                divider = { }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = index == selectedIndex,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.button,
                                color = if (index == selectedIndex)
                                    MaterialTheme.colors.onPrimary else
                                    MaterialTheme.colors.onSurface
                            )
                        },
                        modifier = Modifier
                            .padding(6.dp)
                            .background(
                                shape = MaterialTheme.shapes.small,
                                color = if (index == selectedIndex)
                                    MaterialTheme.colors.primary else
                                    Color.Transparent
                            )
                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.TopCenter
            ) {

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth(), verticalAlignment = Alignment.Top

                ) { page ->
                    when (page) {
                        0 -> {
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(listItems.size) {
                                    ListItem(listItems[it])
                                }

                            }
                        }
                        1 -> {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                listItems.forEach {
                                    ChipItem(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ListItem(text: String) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = text,
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu_option),
                    contentDescription = stringResource(id = R.string.label_icMenuOpton),
                    tint = MaterialTheme.colors.onBackground
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )

        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ChipItem(text: String) {

        Chip(
            modifier = Modifier
                .padding(end = 16.dp)
                .background(Color.Transparent),
            colors = ChipDefaults.chipColors(Color.Transparent),
            onClick = { /*TODO*/ },
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onBackground),

            ) {
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}