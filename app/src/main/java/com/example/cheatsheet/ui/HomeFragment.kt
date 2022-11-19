package com.example.cheatsheet.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.chaetsheet.R
import com.example.cheatsheet.ui.theme.CheatSheetTheme
import com.example.cheatsheet.ui.theme.DarkGray
import com.example.cheatsheet.ui.theme.Gray
import com.example.cheatsheet.ui.theme.White700
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import kotlin.random.Random

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

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun HomeScreen() {
        //set status bar color
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(color = White700)

        val scaffoldState = rememberScaffoldState()
        CheatSheetTheme {
            Scaffold(Modifier.fillMaxSize(), scaffoldState = scaffoldState,
                snackbarHost =
                {
                    SnackbarHost(it) { data ->
                        Snackbar(
                            actionColor = MaterialTheme.colors.primary,
                            backgroundColor = DarkGray,
                            contentColor = White,
                            snackbarData = data
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    GreetingSection(stringResource(id = R.string.label_full_name))
                    Spacer(modifier = Modifier.height(16.dp))
                    TopImage(
                        painter = painterResource(id = R.drawable.mountain),
                        title = stringResource(id = R.string.label_mountain)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ListSection()
                    DashedLineDivider()
                    CheckBoxSection()
                    DashedLineDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                    RadioGroupSection(scaffoldState)
                }
            }

        }
    }

    @Composable
    fun GreetingSection(name: String) {
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

    @OptIn(ExperimentalTextApi::class)
    @Composable
    fun TopImage(painter: Painter, title: String) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(208.dp)
                .border(
                    BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary),
                    shape = MaterialTheme.shapes.large
                )
                .clip(MaterialTheme.shapes.large)
        ) {

            Image(
                painter = painter,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = title,
                style = MaterialTheme.typography.h5.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colors.onPrimary,
                            MaterialTheme.colors.primary
                        )
                    )
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 8.dp)
            )
        }
    }

    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun ListSection() {
        var inputValueState by remember {
            mutableStateOf("")
        }
        val items = remember {
            mutableStateListOf<ListItemModel>()
        }
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InputField(value = inputValueState) {
                inputValueState = it
            }
            Spacer(modifier = Modifier.height(20.dp))
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
                    if (inputValueState.isNotBlank()) {
                        items.add(ListItemModel(text = inputValueState))
                        inputValueState = ""
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                RoundedButton(
                    text = stringResource(id = R.string.label_reset),
                    modifier = Modifier
                        .height(42.dp)
                        .weight(1f),
                ) {
                    if (inputValueState.isNotBlank())
                        inputValueState = ""
                    else
                        items.clear()
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            TabsAndPager(items = items,
                deleteClick = { items.removeAt(it) },
                setRandomColor = { items[it].color = generateRandomColor() })
        }
    }

    @Composable
    fun InputField(value: String, onValueChange: (String) -> Unit) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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
                    text = stringResource(id = R.string.label_type_some_thing),
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

    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun TabsAndPager(
        items: List<ListItemModel>,
        deleteClick: (Int) -> Unit,
        setRandomColor: (Int) -> Unit
    ) {
        val pagerState = rememberPagerState(pageCount = 2)
        val coroutineScope = rememberCoroutineScope()

        val tabTitles = listOf(
            stringResource(id = R.string.label_list),
            stringResource(id = R.string.label_chip)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
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
                        modifier = Modifier
                            .padding(6.dp)
                            .background(
                                shape = MaterialTheme.shapes.small,
                                color = if (index == pagerState.currentPage)
                                    MaterialTheme.colors.primary else
                                    Color.Transparent
                            )
                            .clip(MaterialTheme.shapes.small),
                        selected = index == pagerState.currentPage,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.button,
                                color = if (index == pagerState.currentPage)
                                    MaterialTheme.colors.onPrimary else
                                    MaterialTheme.colors.onSurface
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)

            ) {
                if (items.isEmpty())
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                else
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        when (page) {
                            0 -> {
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(items.size) { index ->
                                        items[index].isShowDivider = (index != items.size - 1)
                                        ListItem(listItemModel = items[index],
                                            deleteClick = { deleteClick(index) },
                                            setRandomColor = { setRandomColor(index) })
                                    }
                                }
                            }
                            1 -> {
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    items.forEach { itemListModel ->
                                        ChipItem(itemListModel.text)
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    @Composable
    fun ListItem(
        listItemModel: ListItemModel,
        deleteClick: () -> Unit,
        setRandomColor: () -> Unit
    ) {
        var isShowingDropDownMenu by remember {
            mutableStateOf(false)
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = listItemModel.text,
                    style = MaterialTheme.typography.body1,
                    color = listItemModel.color
                )

                Box() {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_option),
                        contentDescription = stringResource(id = R.string.label_ic_menu_option),
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.clickable {
                            isShowingDropDownMenu = true
                        }
                    )

                    ShowDropDownMenu(isShowingDropDownMenu, {
                        deleteClick()
                    }, {
                        setRandomColor()
                    }, {
                        isShowingDropDownMenu = false
                    })

                }
            }
            if (listItemModel.isShowDivider)
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
                .padding(end = 12.dp)
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

    @Composable
    fun DashedLineDivider(color: Color = MaterialTheme.colors.primary) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
        Canvas(
            Modifier.fillMaxWidth()
        ) {
            drawLine(
                color = color,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect,
                strokeWidth = 3f
            )
        }
    }

    @Composable
    fun CheckBoxSection() {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            var firstCheckBoxState by remember { mutableStateOf(false) }
            var secondCheckBoxState by remember { mutableStateOf(true) }

            CustomCheckBox(firstCheckBoxState) {
                firstCheckBoxState = firstCheckBoxState.not()
            }
            Spacer(modifier = Modifier.height(8.dp))
            CustomCheckBox(secondCheckBoxState) {
                secondCheckBoxState = secondCheckBoxState.not(
                )
            }
        }
    }

    @SuppressLint("RememberReturnType")
    @Composable
    fun CustomCheckBox(isSelected: Boolean, changeSelected: () -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    changeSelected()
                }
                .padding(8.dp)

        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (isSelected)
                        painterResource(id = R.drawable.ic_tick_checked)
                    else
                        painterResource(id = R.drawable.ic_tick_not_checked),
                    contentDescription = stringResource(id = R.string.label_check_box),

                    tint = if (isSelected)
                        MaterialTheme.colors.primary
                    else
                        MaterialTheme.colors.surface
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (isSelected)
                    stringResource(id = R.string.label_im_checked)
                else
                    stringResource(id = R.string.label_im_not_checked),
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
        }
    }

    @Composable
    fun RadioGroupSection(scaffoldState: ScaffoldState) {
        var selectedIndex by remember {
            mutableStateOf(0)
        }
        var lastSelectedIndex by remember {
            mutableStateOf(0)
        }

        val itemsTitle = listOf("State 1", "State 2", "State 3", "State 4", "State 5", "State 6")
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            columns = GridCells.Fixed(2),
            content = {
                items(itemsTitle.size) {
                    CustomRadioButton(
                        modifier = Modifier.fillMaxWidth(1f),
                        text = itemsTitle[it],
                        index = it,
                        isSelected = selectedIndex == it
                    ) { index ->
                        selectedIndex = index
                        if (index != 5)
                            lastSelectedIndex = index
                    }
                }
            })

        if (selectedIndex == 5) {
            ShowSnackBar(
                stringResource(id = R.string.msg_state_6_choosed),
                stringResource(id = R.string.label_undo),
                scaffoldState
            ) {
                selectedIndex = lastSelectedIndex
            }
        }
    }

    @Composable
    fun CustomRadioButton(
        modifier: Modifier,
        text: String,
        index: Int,
        isSelected: Boolean,
        changeSelected: (Int) -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    changeSelected(index)
                }
                .padding(8.dp)
                .padding(start = if (index % 2 != 0) 24.dp else 0.dp)
        ) {
            Box(modifier = Modifier.size(24.dp)) {
                Icon(
                    painter = if (isSelected)
                        painterResource(id = R.drawable.ic_circle_checked)
                    else
                        painterResource(id = R.drawable.ic_circle_not_checked),
                    contentDescription = stringResource(id = R.string.label_check_box),
                    tint = if (isSelected)
                        MaterialTheme.colors.primary
                    else
                        MaterialTheme.colors.surface
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
        }
    }


    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable
    fun ShowSnackBar(
        text: String,
        actionLabel: String,
        scaffoldState: ScaffoldState,
        action: () -> Unit
    ) {
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            val snackBar = scaffoldState.snackbarHostState.showSnackbar(
                message = text,
                actionLabel = actionLabel
            )
            if (snackBar == SnackbarResult.ActionPerformed)
                action()
        }
    }

    @Composable
    fun ShowDropDownMenu(
        isShowing: Boolean,
        deleteClick: () -> Unit,
        setRandomColor: () -> Unit,
        onDismiss: () -> Unit
    ) {

        DropdownMenu(
            expanded = isShowing,
            onDismissRequest = { onDismiss() },
        ) {

            DropdownMenuItem(onClick = {
                deleteClick()
                onDismiss()
            }) {
                Text(
                    text = stringResource(id = R.string.label_delete),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onSurface
                )
            }

            DropdownMenuItem(onClick = {
                setRandomColor()
                onDismiss()
            }) {
                Text(
                    text = stringResource(id = R.string.label_random_color),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }

    private fun generateRandomColor(): Color {
        return Color(
            Random.nextFloat(),
            Random.nextFloat(),
            Random.nextFloat(),
        )
    }
}


