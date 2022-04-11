package kz.flyingv.remindme.activity.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.flyingv.remindme.model.Reminder

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{MainScreen()}
    }

    @Composable
    private fun MainScreen(viewModel: MainViewModel = viewModel()){
        val materialBlue700= Color(0xFF1976D2)
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val state = viewModel.currentReminders.collectAsState().value
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopAppBar(title = {Text("TopAppBar")}, backgroundColor = materialBlue700)  },
            drawerContent = { Text(text = "drawerContent") },
            content = {
                ReminderList(reminders = state)
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = { FloatingActionButton(onClick = {
                viewModel.createReminder()
                Toast.makeText(this, "reminder set", Toast.LENGTH_LONG).show()
            }){
                Text("X")
            } },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar(
                    backgroundColor = materialBlue700,
                    cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
                    //content =
                ) { Text("BottomAppBar") }
            }
        )
    }

    @Composable
    private fun ReminderList(reminders: List<Reminder>){
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
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