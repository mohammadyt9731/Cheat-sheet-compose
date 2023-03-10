package com.example.cheatsheet.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.chaetsheet.R
import com.example.cheatsheet.ui.model.ListItemModel
import com.example.cheatsheet.ui.navigation.Screens
import com.example.cheatsheet.ui.theme.DarkGray
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost =
        {
            SnackbarHost(it) { data ->
                Snackbar(
                    actionColor = MaterialTheme.colors.primary,
                    backgroundColor = DarkGray,
                    contentColor = MaterialTheme.colors.background,
                    snackbarData = data
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GreetingSection(name = stringResource(id = R.string.label_full_name))
            Spacer(modifier = Modifier.height(16.dp))
            TopImage(
                painter = painterResource(id = R.drawable.mountain),
                title = stringResource(id = R.string.label_mountain)
            )
            Spacer(modifier = Modifier.height(20.dp))
            ListSection(navController)
            DashedLineDivider()
            CheckBoxSection()
            DashedLineDivider()
            Spacer(modifier = Modifier.height(20.dp))
            RadioGroupSection(scaffoldState)
        }
    }
}

@Composable
private fun GreetingSection(name: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
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
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primary
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun TopImage(painter: Painter, title: String) {
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
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = title,
            contentScale = ContentScale.Crop
        )
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.h5.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.primary
                    )
                )
            )
        )
    }
}

@Composable
private fun ListSection(navController: NavHostController) {
    var inputValueState by rememberSaveable {
        mutableStateOf("")
    }

    val listItemSaver = listSaver<MutableList<ListItemModel>, ListItemModel>(
        save = { it.toList() },
        restore = { it.toMutableStateList() }
    )
    val items = rememberSaveable(saver = listItemSaver) {
        mutableStateListOf()
    }

    InputField(value = inputValueState) {
        inputValueState = it
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RoundedButton(
            modifier = Modifier
                .height(42.dp)
                .weight(1f),
            text = stringResource(id = R.string.label_add),
        ) {
            if (inputValueState.isNotBlank()) {
                items.add(ListItemModel(text = inputValueState))
                inputValueState = ""
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        RoundedButton(
            modifier = Modifier
                .height(42.dp)
                .weight(1f),
            text = stringResource(id = R.string.label_reset),
        ) {
            if (inputValueState.isNotBlank()) {
                inputValueState = ""
            } else {
                items.clear()
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    TabsAndPager(
        items = items,
        deleteClick = { items.removeAt(it) },
        setRandomColor = { items[it] = items[it].copy(color = generateRandomColor()) },
        rootClick = {
            navController.navigate(
                Screens.DetailScreen.withArg(items[it].text)
            )
        }
    )
}

@Composable
private fun InputField(value: String, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        value = value,
        onValueChange = { onValueChange(it) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        textStyle = MaterialTheme.typography.body1,
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
private fun RoundedButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
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
private fun TabsAndPager(
    items: List<ListItemModel>,
    deleteClick: (Int) -> Unit,
    setRandomColor: (Int) -> Unit,
    rootClick: (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = 2)
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf(
        stringResource(id = R.string.label_list),
        stringResource(id = R.string.label_chip)
    )

    TabRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colors.surface
            ), backgroundColor = Color.Transparent,
        selectedTabIndex = pagerState.currentPage,
        indicator = { },
        divider = { }
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier
                    .padding(6.dp)
                    .background(
                        shape = MaterialTheme.shapes.small,
                        color = if (index == pagerState.currentPage) {
                            MaterialTheme.colors.primary
                        } else {
                            Color.Transparent
                        }
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
                        color = if (index == pagerState.currentPage) {
                            MaterialTheme.colors.onPrimary
                        } else {
                            MaterialTheme.colors.onSurface
                        }
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
        if (items.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp, bottom = 4.dp),
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    0 -> {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(items.size) { index ->
                                ListItem(listItemModel = items[index],
                                    rootClick = { rootClick(index) },
                                    deleteClick = { deleteClick(index) },
                                    setRandomColor = { setRandomColor(index) })
                                if ((index != items.size - 1)) {
                                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                                }
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
private fun ListItem(
    listItemModel: ListItemModel,
    rootClick: () -> Unit,
    deleteClick: () -> Unit,
    setRandomColor: () -> Unit
) {
    var isShowingDropDownMenu by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable { rootClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = listItemModel.text,
            style = MaterialTheme.typography.body1,
            color = listItemModel.color
        )
        Box() {
            Icon(
                modifier = Modifier.clickable {
                    isShowingDropDownMenu = true
                },
                painter = painterResource(id = R.drawable.ic_menu_option),
                contentDescription = stringResource(id = R.string.label_ic_menu_option),
                tint = MaterialTheme.colors.onBackground
            )

            ShowDropDownMenu(
                isShowing = isShowingDropDownMenu,
                deleteClick = { deleteClick() },
                setRandomColor = { setRandomColor() },
                onDismiss = { isShowingDropDownMenu = false })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ChipItem(text: String) {
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
private fun DashedLineDivider(color: Color = MaterialTheme.colors.primary) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
    Canvas(
        modifier = Modifier.fillMaxWidth()
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
private fun CheckBoxSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        var firstCheckBoxState by rememberSaveable { mutableStateOf(false) }
        var secondCheckBoxState by rememberSaveable { mutableStateOf(true) }

        CustomCheckBox(firstCheckBoxState) {
            firstCheckBoxState = firstCheckBoxState.not()
        }
        Spacer(modifier = Modifier.height(8.dp))
        CustomCheckBox(secondCheckBoxState) {
            secondCheckBoxState = secondCheckBoxState.not()
        }
    }
}

@Composable
private fun CustomCheckBox(isSelected: Boolean, changeSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable {
                changeSelected()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = if (isSelected) {
                    painterResource(id = R.drawable.ic_tick_checked)
                } else {
                    painterResource(id = R.drawable.ic_tick_not_checked)
                },
                contentDescription = stringResource(id = R.string.label_check_box),

                tint = if (isSelected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.surface
                }
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = if (isSelected) {
                stringResource(id = R.string.label_im_checked)
            } else {
                stringResource(id = R.string.label_im_not_checked)
            },
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun RadioGroupSection(scaffoldState: ScaffoldState) {
    var selectedIndex by rememberSaveable {
        mutableStateOf(0)
    }
    var lastSelectedIndex by rememberSaveable {
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
                    if (index != 5) {
                        lastSelectedIndex = index
                    }
                }
            }
        })

    if (selectedIndex == 5) {
        ShowSnackBar(
            text = stringResource(id = R.string.msg_state_6_choosed),
            actionLabel = stringResource(id = R.string.label_undo),
            isShowing = selectedIndex == 5,
            scaffoldState = scaffoldState
        ) {
            selectedIndex = lastSelectedIndex
        }
    }
}

@Composable
private fun CustomRadioButton(
    modifier: Modifier,
    text: String,
    index: Int,
    isSelected: Boolean,
    changeSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .clickable {
                changeSelected(index)
            }
            .padding(8.dp)
            .padding(
                start = if (index % 2 != 0) {
                    24.dp
                } else {
                    0.dp
                }
            )
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = if (isSelected) {
                painterResource(id = R.drawable.ic_circle_checked)
            } else {
                painterResource(id = R.drawable.ic_circle_not_checked)
            },
            contentDescription = stringResource(id = R.string.label_check_box),
            tint = if (isSelected) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.surface
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun ShowSnackBar(
    text: String,
    actionLabel: String,
    scaffoldState: ScaffoldState,
    isShowing: Boolean,
    action: () -> Unit
) {
    LaunchedEffect(key1 = isShowing) {
        val snackBar = scaffoldState.snackbarHostState.showSnackbar(
            message = text,
            actionLabel = actionLabel
        )
        if (snackBar == SnackbarResult.ActionPerformed) {
            action()
        }
    }
}

@Composable
private fun ShowDropDownMenu(
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
        (0..255).random(),
        (0..255).random(),
        (0..255).random()
    )
}