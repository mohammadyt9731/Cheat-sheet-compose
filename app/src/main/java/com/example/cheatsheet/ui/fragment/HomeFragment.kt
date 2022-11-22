package com.example.cheatsheet.ui.fragment

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.fragment.findNavController
import com.example.chaetsheet.R
import com.example.cheatsheet.ui.model.ListItemModel
import com.example.cheatsheet.ui.theme.CheatSheetTheme
import com.example.cheatsheet.ui.theme.DarkGray
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun HomeScreen() {
        //set status bar color
        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(color = MaterialTheme.colors.background)

        val scaffoldState = rememberScaffoldState()
        CheatSheetTheme {
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
    fun ListSection() {
        var inputValueState by rememberSaveable {
            mutableStateOf("")
        }
        val items = rememberMutableStateListOf<ListItemModel>()

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
        TabsAndPager(items = items,
            deleteClick = { items.removeAt(it) },
            setRandomColor = { items[it] = items[it].copy(color = generateRandomColor()) })
    }

    @Composable
    fun InputField(value: String, onValueChange: (String) -> Unit) {
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
                                        rootClick = {
                                            val directions =
                                                HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                                                    items[index].text
                                                )
                                            findNavController().navigate(directions)
                                        },
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
    fun ListItem(
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
    fun CheckBoxSection() {
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
    fun CustomCheckBox(isSelected: Boolean, changeSelected: () -> Unit) {
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
    fun RadioGroupSection(scaffoldState: ScaffoldState) {
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
    fun CustomRadioButton(
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
    fun ShowSnackBar(
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
            (0..255).random(),
            (0..255).random(),
            (0..255).random()
        )
    }

    @Composable
    fun <T : Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
        return rememberSaveable(
            saver = listSaver(
                save = { stateList ->
                    if (stateList.isNotEmpty()) {
                        val first = stateList.first()
                        if (!canBeSaved(first)) {
                            throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                        }
                    }
                    stateList.toList()
                },
                restore = { it.toMutableStateList() }
            )
        ) {
            elements.toList().toMutableStateList()
        }
    }
}
