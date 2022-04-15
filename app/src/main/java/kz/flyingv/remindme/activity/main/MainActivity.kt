package kz.flyingv.remindme.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kz.flyingv.remindme.activity.main.action.MainAction
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.ui.selector.SegmentText
import kz.flyingv.remindme.ui.selector.SegmentedControl
import kz.flyingv.remindme.ui.topbar.CustomTopBar

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

    @Composable
    private fun CreateScaffold(showNewReminderDialog: () -> Unit){
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val materialBlue700 = Color(0xFF1976D2)

        val mainState = viewModel.mainStateFlow.collectAsState().value

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                CustomTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    onSearchStarted = {viewModel.makeAction(MainAction.StartSearch)},
                    onSearchUpdate = {text -> viewModel.makeAction(MainAction.UpdateSearch(text))},
                    onSearchClose = {viewModel.makeAction(MainAction.EndSearch)},
                    isSearching = mainState.isSearching,
                    searchValue = mainState.searchText
                )
            },
            drawerGesturesEnabled = false,
            content = { ReminderList(reminders = mainState.reminders) },
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun NewReminderDialog(dialogState: ModalBottomSheetState){

        val scope = rememberCoroutineScope()

        val remindTypes = remember { listOf("Daily", "Weekly", "Monthly", "Yearly") }
        var selectedThreeSegment by remember { mutableStateOf(remindTypes.first()) }

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(text ="NEW REMINDER", style = typography.h6)
            Spacer(modifier = Modifier.height(16.dp))

            SegmentedControl(
                remindTypes,
                selectedThreeSegment,
                onSegmentSelected = { selectedThreeSegment = it }
            ) {
                SegmentText(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                singleLine = true,
                onValueChange = {},
                placeholder = { Text("Reminder Name") },
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconSelector()
            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(16.dp))
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
    private fun ReminderList(reminders: List<Reminder>){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 8.dp, end = 8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                count = reminders.size,
                itemContent = {
                    ReminderListItem(reminder = reminders[it])
                }
            )
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
            Row(modifier = Modifier.padding(8.dp)) {
                Column {
                    Text(text = "fdgdfg", style = typography.h6)
                    Text(text = reminder.name, style = typography.h6)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "VIEW DETAIL", style = typography.caption)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "VIEW DETAIL", style = typography.caption)
                }
            }
        }
    }

    @Composable
    fun IconSelector(){
        var selectIcon by remember { mutableStateOf(Icons.Filled.ThumbUp) }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Box(modifier = Modifier.shadow(4.dp, CircleShape).background(if(selectIcon == Icons.Filled.ThumbUp){Color.Cyan}else{Color.White}, CircleShape).clickable{selectIcon = Icons.Filled.ThumbUp}){ Icon(Icons.Filled.ThumbUp, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.shadow(4.dp, CircleShape).background(if(selectIcon == Icons.Filled.ArrowBack){Color.Cyan}else{Color.White}, CircleShape).clickable{selectIcon = Icons.Filled.ArrowBack}){ Icon(Icons.Filled.ArrowBack, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.shadow(4.dp, CircleShape).background(if(selectIcon == Icons.Filled.AddCircle){Color.Cyan}else{Color.White}, CircleShape).clickable{selectIcon = Icons.Filled.AddCircle}){ Icon(Icons.Filled.AddCircle, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.shadow(4.dp, CircleShape).background(if(selectIcon == Icons.Filled.Build){Color.Cyan}else{Color.White}, CircleShape).clickable{selectIcon = Icons.Filled.Build}){ Icon(Icons.Filled.Build, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.shadow(4.dp, CircleShape).background(if(selectIcon == Icons.Filled.Call){Color.Cyan}else{Color.White}, CircleShape).clickable{selectIcon = Icons.Filled.Call}){ Icon(Icons.Filled.Call, "", Modifier.padding(12.dp).width(24.dp).height(24.dp)) }
        }
    }

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