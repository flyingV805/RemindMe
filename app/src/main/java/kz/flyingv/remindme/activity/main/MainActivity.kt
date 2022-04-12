package kz.flyingv.remindme.activity.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.ui.selector.SegmentText
import kz.flyingv.remindme.ui.selector.SegmentedControl

class MainActivity : ComponentActivity() {

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
    private fun MainScreen(viewModel: MainViewModel = viewModel()){
        val modalBottomState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()
        ModalBottomSheetLayout(
            sheetState = modalBottomState,
            sheetShape = shapes.large.copy(topStart = CornerSize(16.dp), topEnd = CornerSize(16.dp)),
            sheetContent = { NewReminderDialog() }
        ) {
            CreateScaffold(viewModel){
                scope.launch { modalBottomState.show() }
            }
        }
    }

    @Composable
    private fun CreateScaffold(viewModel: MainViewModel, addNew: () -> Job){

        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val materialBlue700= Color(0xFF1976D2)
        val scope = rememberCoroutineScope()

        val state = viewModel.currentReminders.collectAsState().value
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { CreateTopBar()/*TopAppBar(title = {Text("TopAppBar")}, backgroundColor = materialBlue700)  */},
            //drawerContent = { Text(text = "drawerContent") },
            drawerGesturesEnabled = false,
            content = { ReminderList(reminders = state) },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = { FloatingActionButton(onClick = {
                addNew()
                //drawerState.open()
                //viewModel.createReminder()
                //Toast.makeText(this, "reminder set", Toast.LENGTH_LONG).show()
            }){
                Text("X")
            } },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar(
                    backgroundColor = materialBlue700,
                    cutoutShape = shapes.small.copy(CornerSize(percent = 50)),
                ) { Text("BottomAppBar") }
            }
        )
    }

    @Composable
    private fun CreateTopBar(){
        Box(
            modifier = Modifier.fillMaxWidth().height(56.dp)
                .shadow(4.dp, RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp)),

        ) {

        }
    }

    @Composable
    private fun NewReminderDialog(){

        val threeSegments = remember { listOf("Daily", "Weekly", "Monthly", "Yearly") }
        var selectedThreeSegment by remember { mutableStateOf(threeSegments.first()) }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text ="NEW REMINDER", style = typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            SegmentedControl(
                threeSegments,
                selectedThreeSegment,
                onSegmentSelected = { selectedThreeSegment = it }
            ) {
                SegmentText(it)
            }
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
            FloatingActionButton(onClick = {}) {
                Text("CREATE REMINDER")
            }
        }
    }

    @Composable
    private fun ReminderList(reminders: List<Reminder>){
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
        Row {
            Column {
                Text(text = reminder.name, style = typography.h6)
                Text(text = "VIEW DETAIL", style = typography.caption)
            }
        }
    }

    @Preview
    @Composable
    fun ComposablePreview() {
        MainScreen()
    }

}