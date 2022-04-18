package kz.flyingv.remindme.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kz.flyingv.remindme.R
import kz.flyingv.remindme.activity.main.action.MainAction
import kz.flyingv.remindme.activity.main.state.RemindTypeEnum
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.ui.iconselector.DayOfMonthSelector
import kz.flyingv.remindme.ui.iconselector.DayOfWeekSelector
import kz.flyingv.remindme.ui.iconselector.DayOfYearSelector
import kz.flyingv.remindme.ui.iconselector.IconSelector
import kz.flyingv.remindme.ui.isInPreview
import kz.flyingv.remindme.ui.previewState
import kz.flyingv.remindme.ui.selector.SegmentText
import kz.flyingv.remindme.ui.selector.SegmentedControl
import kz.flyingv.remindme.ui.topbar.CustomTopBar
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            MaterialTheme {
                MainScreen()
            }
        }

    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun MainScreen(){
        val modalBottomState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {it != ModalBottomSheetValue.HalfExpanded}
        )
        val scope = rememberCoroutineScope()
        ModalBottomSheetLayout(
            sheetState = modalBottomState,
            sheetShape = shapes.large.copy(topStart = CornerSize(16.dp), topEnd = CornerSize(16.dp)),
            sheetContent = { NewReminderDialog(modalBottomState) }
        ) {
            BackHandler(enabled = modalBottomState.isVisible) {
                scope.launch {modalBottomState.hide()}
            }
            CreateScaffold{
               scope.launch {
                   modalBottomState.animateTo(ModalBottomSheetValue.Expanded)
               }
            }
        }
    }


    private val topBarHeight = 72.dp

    @Composable
    private fun CreateScaffold(showNewReminderDialog: () -> Unit){
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val materialBlue700 = Color(0xFF1976D2)

        val mainState = if(!isInPreview()){
            viewModel.mainStateFlow.collectAsState().value
        }else{
            previewState()
        }

        val toolbarHeightPx = with(LocalDensity.current) { topBarHeight.roundToPx().toFloat() }
        val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                    return Offset.Zero
                }
            }
        }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
            },
            drawerGesturesEnabled = false,
            content = {
                Box(modifier = Modifier.nestedScroll(nestedScrollConnection)) {
                    ReminderList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 8.dp, end = 8.dp),
                        reminders = mainState.reminders,
                    )
                    CustomTopBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(topBarHeight)
                            .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
                        onSearchStarted = {viewModel.makeAction(MainAction.StartSearch)},
                        onSearchUpdate = {text -> viewModel.makeAction(MainAction.UpdateSearch(text))},
                        onSearchClose = {viewModel.makeAction(MainAction.EndSearch)},
                        isSearching = mainState.isSearching,
                        searchValue = mainState.searchText
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showNewReminderDialog()
                    }
                ){
                    Icon(Icons.Filled.Add, "")
                }
            },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar(
                    backgroundColor = materialBlue700,
                    cutoutShape = shapes.small.copy(CornerSize(percent = 50)),
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.AccountCircle, "")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.AccountBox, "")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Settings, "")
                        }
                    }
                }
            }
        )

    }

    private val iconList = listOf(R.drawable.ic_avatar_cake, R.drawable.ic_avatar_medeicine, R.drawable.ic_avatar_officials, R.drawable.ic_avatar_payday, R.drawable.ic_avatar_workout)

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun NewReminderDialog(dialogState: ModalBottomSheetState){

        val scope = rememberCoroutineScope()

        val remindTypes = remember { listOf(RemindTypeEnum.Daily, RemindTypeEnum.Weekly, RemindTypeEnum.Monthly, RemindTypeEnum.Yearly) }
        var selectedRemindType by remember { mutableStateOf(RemindTypeEnum.Daily) }
        var selectIcon by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(text ="NEW REMINDER", style = typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            IconSelector(
                modifier = Modifier.fillMaxWidth(),
                icons = iconList,
                onSelectionChanged = {selectIconIndex -> selectIcon = selectIconIndex}
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                singleLine = true,
                onValueChange = {},
                placeholder = { Text("Reminder Name") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            SegmentedControl(
                remindTypes,
                selectedRemindType,
                onSegmentSelected = { selectedRemindType = it }
            ) {
                SegmentText(
                    when(it){
                        RemindTypeEnum.Daily -> "Daily"
                        RemindTypeEnum.Weekly -> "Weekly"
                        RemindTypeEnum.Monthly -> "Monthly"
                        RemindTypeEnum.Yearly -> "Yearly"
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                contentAlignment = Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    enter = fadeIn(), exit = fadeOut(),
                    visible = selectedRemindType == RemindTypeEnum.Daily
                ) {
                    Text("Remind every day")
                }
                androidx.compose.animation.AnimatedVisibility(
                    enter = fadeIn(), exit = fadeOut(),
                    visible = selectedRemindType == RemindTypeEnum.Weekly
                ) {
                    DayOfWeekSelector(
                        modifier = Modifier.fillMaxWidth(),
                        onSelectionChanged = {}
                    )
                }
                androidx.compose.animation.AnimatedVisibility(
                    enter = fadeIn(), exit = fadeOut(),
                    visible = selectedRemindType == RemindTypeEnum.Monthly
                ) {
                    DayOfMonthSelector(
                        modifier = Modifier.fillMaxWidth(),
                        onSelectionChanged = {}
                    )
                }
                androidx.compose.animation.AnimatedVisibility(
                    enter = fadeIn(), exit = fadeOut(),
                    visible = selectedRemindType == RemindTypeEnum.Yearly
                ) {
                    DayOfYearSelector(
                        modifier = Modifier.fillMaxWidth(),
                        onSelectionChanged = {day, month ->  }
                    )
                }
            }

            Text(text ="VIEW DETAIL", style = typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "VIEW DETAIL", style = typography.caption)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text ="VIEW DETAIL", style = typography.h6)
            Text(text = "VIEW DETAIL", style = typography.caption)
            Text(text ="VIEW DETAIL", style = typography.h6)
            Text(text = "VIEW DETAIL", style = typography.caption)
            Spacer(modifier = Modifier.height(16.dp))
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.Create,"") },
                text = { Text("CREATE REMINDER") },
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    scope.launch { dialogState.hide() }
                    viewModel.createReminder()
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    @Composable
    private fun ReminderList(modifier: Modifier, reminders: List<Reminder>){
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(top = topBarHeight, start = 0.dp, end = 0.dp, bottom = 8.dp)
        ) {
            items(
                count = reminders.size,
                itemContent = {
                    ReminderListItem(reminder = reminders[it])
                }
            )
            item{
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }

    @Composable
    fun ReminderListItem(reminder: Reminder) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable { },
            elevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(56.dp)
                        .height(56.dp)
                        .background(Color.Blue, CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(Icons.Filled.ThumbUp, "",
                        Modifier
                            .padding(12.dp)
                            .width(24.dp)
                            .height(24.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(text = "Grandmas birthday", style = typography.h6)
                    Text(text = "Every month, 6-th", style = typography.h6)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Last remind: 2 days ago", style = typography.caption)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Action: Open app - Kaspi.kz", style = typography.caption)
                }
            }
        }
    }
/*
    @Composable
    fun IconSelector(){
        var selectIcon by remember { mutableStateOf(Icons.Filled.ThumbUp) }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Box(modifier = Modifier
                .shadow(4.dp, CircleShape)
                .background(
                    if (selectIcon == Icons.Filled.ThumbUp) {
                        Color.Cyan
                    } else {
                        Color.White
                    }, CircleShape
                )
                .clickable { selectIcon = Icons.Filled.ThumbUp }){ Icon(Icons.Filled.ThumbUp, "",
                Modifier
                    .padding(12.dp)
                    .width(24.dp)
                    .height(24.dp)) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier
                .shadow(4.dp, CircleShape)
                .background(
                    if (selectIcon == Icons.Filled.ArrowBack) {
                        Color.Cyan
                    } else {
                        Color.White
                    }, CircleShape
                )
                .clickable { selectIcon = Icons.Filled.ArrowBack }){ Icon(Icons.Filled.ArrowBack, "",
                Modifier
                    .padding(12.dp)
                    .width(24.dp)
                    .height(24.dp)) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier
                .shadow(4.dp, CircleShape)
                .background(
                    if (selectIcon == Icons.Filled.AddCircle) {
                        Color.Cyan
                    } else {
                        Color.White
                    }, CircleShape
                )
                .clickable { selectIcon = Icons.Filled.AddCircle }){ Icon(Icons.Filled.AddCircle, "",
                Modifier
                    .padding(12.dp)
                    .width(24.dp)
                    .height(24.dp)) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier
                .shadow(4.dp, CircleShape)
                .background(
                    if (selectIcon == Icons.Filled.Build) {
                        Color.Cyan
                    } else {
                        Color.White
                    }, CircleShape
                )
                .clickable { selectIcon = Icons.Filled.Build }){ Icon(Icons.Filled.Build, "",
                Modifier
                    .padding(12.dp)
                    .width(24.dp)
                    .height(24.dp)) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier
                .shadow(4.dp, CircleShape)
                .background(
                    if (selectIcon == Icons.Filled.Call) {
                        Color.Cyan
                    } else {
                        Color.White
                    }, CircleShape
                )
                .clickable { selectIcon = Icons.Filled.Call }){ Icon(Icons.Filled.Call, "",
                Modifier
                    .padding(12.dp)
                    .width(24.dp)
                    .height(24.dp)) }
        }
    }
*/
    @Preview
    @Composable
    fun ComposablePreview() {
        MaterialTheme {
            MainScreen()
        }
    }

/*
    Box(modifier = Modifier.shadow(4.dp, RoundedCornerShape(8.dp))
    .background(Color.White, RoundedCornerShape(8.dp))
    ){
        IconButton(onClick = {isDropdownOpen = true}) {
            Icon(selectIcon, "")
        }
        DropdownMenu(
            expanded = isDropdownOpen,
            onDismissRequest = { isDropdownOpen = false }
        ){
            Box(modifier = Modifier.clickable{isDropdownOpen = false; selectIcon = Icons.Filled.ThumbUp}){ Icon(Icons.Filled.ThumbUp, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Box(modifier = Modifier.clickable{isDropdownOpen = false; selectIcon = Icons.Filled.AccountBox}){ Icon(Icons.Filled.AccountBox, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Box(modifier = Modifier.clickable{isDropdownOpen = false; selectIcon = Icons.Filled.Search}){ Icon(Icons.Filled.Search, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Box(modifier = Modifier.clickable{isDropdownOpen = false; selectIcon = Icons.Filled.Call}){ Icon(Icons.Filled.Call, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Box(modifier = Modifier.clickable{isDropdownOpen = false; selectIcon = Icons.Filled.Check}){ Icon(Icons.Filled.Check, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Box(modifier = Modifier.clickable{isDropdownOpen = false; selectIcon = Icons.Filled.ThumbUp}){ Icon(Icons.Filled.ThumbUp, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
        }
    }
    */
}