package kz.flyingv.remindme.features.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.flyingv.remindme.features.create.ui.AppSelector
import kz.flyingv.remindme.features.create.ui.DayOfMonthSelector
import kz.flyingv.remindme.features.create.ui.DayOfWeekSelector
import kz.flyingv.remindme.features.create.ui.DayOfYearSelector
import kz.flyingv.remindme.features.create.ui.IconSelector
import kz.flyingv.remindme.features.create.ui.SegmentText
import kz.flyingv.remindme.features.create.ui.SegmentedControl
import kz.flyingv.remindme.features.create.uidata.RemindAction
import kz.flyingv.remindme.features.create.uidata.RemindType
import kz.flyingv.remindme.features.create.uidata.ValidationError

@Composable
fun NewRemindScreen(
    viewModel: NewRemindViewModel = viewModel(),
    onHide: () -> Unit
) {

    val viewState by viewModel.provideState().collectAsStateWithLifecycle()

    val dayOfMonthScrollState = rememberLazyListState()

    LaunchedEffect(key1 = viewState.done){
        if(viewState.done){
            onHide()
            viewModel.reduce(NewRemindAction.Hidden)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Reminder icon
        IconSelector(
            currentSelect = viewState.icon,
            onSelectionChanged = { viewModel.reduce(NewRemindAction.UpdateIcon(it)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Reminder name
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = viewState.name,
            singleLine = true,
            isError = viewState.errors.contains(ValidationError.NeedName),
            onValueChange = { viewModel.reduce(NewRemindAction.UpdateName(it)) },
            placeholder = { Text("Reminder Name") },
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )

        AnimatedVisibility(visible = viewState.errors.contains(ValidationError.NeedName)) {
            Text(
                text = "You need to name it, trust me",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        //Reminder type
        SegmentedControl(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            segments = listOf(RemindType.Daily, RemindType.Weekly, RemindType.Monthly, RemindType.Yearly),
            selectedSegment = viewState.type,
            onSegmentSelected = {
                viewModel.reduce(NewRemindAction.UpdateType(it))
            }
        ) {
            SegmentText(
                when(it){
                    RemindType.Daily -> "Daily"
                    RemindType.Weekly -> "Weekly"
                    RemindType.Monthly -> "Monthly"
                    RemindType.Yearly -> "Yearly"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Reminder type options
        Crossfade(
            targetState = viewState.type,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            label = "",
        ) { remindType ->
            when(remindType){
                RemindType.Daily -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Remind every day!")
                }
                RemindType.Weekly -> DayOfWeekSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDay = viewState.dayOfWeek,
                    onSelectionChanged = {
                        viewModel.reduce(NewRemindAction.UpdateDayOfWeek(it))
                    }
                )
                RemindType.Monthly -> DayOfMonthSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectDay = viewState.dayOfMonth,
                    scrollState = dayOfMonthScrollState,
                    onSelectionChanged = {
                        viewModel.reduce(NewRemindAction.UpdateDayOfMonth(it))
                    }
                )
                RemindType.Yearly -> DayOfYearSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedMonth = viewState.monthOfYear,
                    selectedDay = viewState.dayOfYear,
                    onSelectionChanged = {day, month ->
                        viewModel.reduce(NewRemindAction.UpdateDayOfYear(day, month))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SegmentedControl(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            segments = listOf(RemindAction.Nothing, RemindAction.OpenApp, RemindAction.OpenUrl),
            selectedSegment = viewState.action,
            onSegmentSelected = {
                viewModel.reduce(NewRemindAction.UpdateAction(it))
            }
        ) {
            SegmentText(
                when(it){
                    RemindAction.Nothing -> "Nothing"
                    RemindAction.OpenApp -> "Open App"
                    RemindAction.OpenUrl -> "Open Link"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Crossfade(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            targetState = viewState.action,
            label = ""
        ) {
            when(it){
                RemindAction.Nothing ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "OK, no actions")
                    }
                RemindAction.OpenApp -> AppSelector(
                    apps = viewState.availableApps,
                    selectedApp = viewState.actionApp,
                    onSelectionChanged = { app ->
                        viewModel.reduce(NewRemindAction.UpdateApp(app))
                    }
                )
                RemindAction.OpenUrl -> TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 16.dp, end = 16.dp),
                    value = viewState.actionUrl,
                    singleLine = true,
                    onValueChange = { url ->
                        viewModel.reduce(NewRemindAction.UpdateLink(url))
                    },
                    placeholder = { Text("Enter URL") },
                )
            }
        }

        AnimatedVisibility(visible = viewState.errors.contains(ValidationError.NeedApp)) {
            Text(
                text = "Need to pick an app",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        AnimatedVisibility(visible = viewState.errors.contains(ValidationError.NeedLink)) {
            Text(
                text = "You should set a link, to use it",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExtendedFloatingActionButton(
            onClick = {
                viewModel.reduce( NewRemindAction.Create )
            }
        ) {
            Icon(Icons.Filled.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text("CREATE REMINDER")
        }

        Spacer(modifier = Modifier.height(32.dp))

    }


}