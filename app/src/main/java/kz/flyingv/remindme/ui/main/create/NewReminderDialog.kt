package kz.flyingv.remindme.ui.main.create

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kz.flyingv.remindme.R
import kz.flyingv.remindme.data.model.*
import kz.flyingv.remindme.ui.statemodel.RemindActionEnum
import kz.flyingv.remindme.ui.statemodel.RemindTypeEnum
import kz.flyingv.remindme.ui.statemodel.ValidationError
import kz.flyingv.remindme.ui.widgets.selector.*
import kz.flyingv.remindme.ui.widgets.isInPreview
import kz.flyingv.remindme.ui.widgets.previewNewReminderState
import kz.flyingv.remindme.ui.widgets.selector.AppSelector
import kz.flyingv.remindme.ui.widgets.selector.IconSelector
import kz.flyingv.remindme.ui.widgets.selector.SegmentText
import kz.flyingv.remindme.ui.widgets.selector.SegmentedControl

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewReminderDialog(viewModel: NewReminderViewModel = viewModel(), close: () -> Unit?){

    val scope = rememberCoroutineScope()

    val newReminderState = if(!isInPreview()){
        viewModel.newReminderStateFlow.collectAsState().value
    }else{
        previewNewReminderState()
    }

    if(newReminderState.error == ValidationError.Created){
        close()
        //scope.launch { dialogState.hide() }
    }

    val remindTypes = remember { listOf(RemindTypeEnum.Daily, RemindTypeEnum.Weekly, RemindTypeEnum.Monthly, RemindTypeEnum.Yearly) }
    val remindActions = remember { listOf(RemindActionEnum.Nothing, RemindActionEnum.OpenApp, RemindActionEnum.OpenUrl) }

    val remindTypeEnum = when(newReminderState.type){
        is RemindType.Daily -> RemindTypeEnum.Daily
        is RemindType.Monthly -> RemindTypeEnum.Monthly
        is RemindType.Weekly -> RemindTypeEnum.Weekly
        is RemindType.Yearly -> RemindTypeEnum.Yearly
    }

    val remindActionEnum = when(newReminderState.action){
        is RemindAction.DoNothing -> RemindActionEnum.Nothing
        is RemindAction.OpenApp -> RemindActionEnum.OpenApp
        is RemindAction.OpenUrl -> RemindActionEnum.OpenUrl
    }

    //remember last selectors state for seamless transition
    val lastSelectedDayOfWeek = remember{ mutableStateOf(DayOfWeek.MONDAY) }
    val lastSelectedDayOfMonth = remember{ mutableStateOf(1) }
    val daysOfMonthScrollState = remember{ LazyListState() }
    val selectedApp = remember {mutableStateOf<InstalledApp?>(null)}
    val selectedUrl = remember {mutableStateOf<String?>(null)}
    val lastSelectedMonthOfYear = remember{ mutableStateOf(MonthOfYear.January) }
    val lastSelectedDayOfMonthInYear = remember{ mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text ="NEW REMINDER", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))
        //Reminder icon
        IconSelector(
            modifier = Modifier.fillMaxWidth(),
            currentSelect = newReminderState.icon,
            onSelectionChanged = {selectIcon ->
                viewModel.makeAction(
                    NewReminderAction.UpdateIcon(selectIcon)
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        //Reminder name
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = newReminderState.name,
            singleLine = true,
            isError = newReminderState.error == ValidationError.NeedName,
            onValueChange = {
                viewModel.makeAction(NewReminderAction.UpdateName(it))
            },
            placeholder = { Text("Reminder Name") },
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )
        AnimatedVisibility(visible = newReminderState.error == ValidationError.NeedName) {
            Text(
                text = "You need to name it, trust me",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        //Reminder type
        SegmentedControl(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            segments = remindTypes,
            selectedSegment = remindTypeEnum,
            onSegmentSelected = {
                when(it){
                    RemindTypeEnum.Daily -> {
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Daily))
                    }
                    RemindTypeEnum.Weekly -> {
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Weekly(lastSelectedDayOfWeek.value)))
                    }
                    RemindTypeEnum.Monthly -> {
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Monthly(lastSelectedDayOfMonth.value)))
                    }
                    RemindTypeEnum.Yearly -> {
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Yearly(lastSelectedDayOfMonthInYear.value, lastSelectedMonthOfYear.value)))
                    }
                }
            }
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

        //Reminder type options
        Crossfade(
            targetState = newReminderState.type,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        ) { remindType ->
            when(remindType){
                is RemindType.Daily -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Remind every day!")
                }
                is RemindType.Weekly -> DayOfWeekSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDay = (newReminderState.type as? RemindType.Weekly)?.dayOfWeek ?: lastSelectedDayOfWeek.value,
                    onSelectionChanged = {
                        lastSelectedDayOfWeek.value = it
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Weekly(it)))
                    }
                )
                is RemindType.Monthly -> DayOfMonthSelector(
                    modifier = Modifier.fillMaxWidth(),
                    scrollState = daysOfMonthScrollState,
                    selectDay = (newReminderState.type as? RemindType.Monthly)?.dayOfMonth ?: lastSelectedDayOfMonth.value,
                    onSelectionChanged = {
                        lastSelectedDayOfMonth.value = it
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Monthly(it)))
                    }
                )
                is RemindType.Yearly -> DayOfYearSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedMonth = (newReminderState.type as? RemindType.Yearly)?.month ?: lastSelectedMonthOfYear.value,
                    selectedDay = (newReminderState.type as? RemindType.Yearly)?.dayOfMonth ?: lastSelectedDayOfMonthInYear.value,
                    onSelectionChanged = {day, month ->
                        lastSelectedDayOfMonthInYear.value = day
                        lastSelectedMonthOfYear.value = month
                        Log.d("UpdateType", "$day and $month")
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Yearly(dayOfMonth = day, month = month)))
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        SegmentedControl(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            segments = remindActions,
            selectedSegment = remindActionEnum,
            onSegmentSelected = {
                when(it){
                    RemindActionEnum.Nothing -> {
                        viewModel.makeAction(NewReminderAction.UpdateAction(RemindAction.DoNothing))
                    }
                    RemindActionEnum.OpenApp -> {
                        viewModel.makeAction(NewReminderAction.UpdateAction(RemindAction.OpenApp(selectedApp.value)))
                    }
                    RemindActionEnum.OpenUrl -> {
                        viewModel.makeAction(NewReminderAction.UpdateAction(RemindAction.OpenUrl(selectedUrl.value)))
                    }
                }
            }
        ) {
            SegmentText(
                when(it){
                    RemindActionEnum.Nothing -> "Nothing"
                    RemindActionEnum.OpenApp -> "Open App"
                    RemindActionEnum.OpenUrl -> "Open Link"
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Crossfade(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            targetState = newReminderState.action
        ) {
            when(it){
                is RemindAction.DoNothing ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "OK, no actions")
                    }
                is RemindAction.OpenApp -> AppSelector(
                    apps = newReminderState.actionApps,
                    selectedApp = it.installedApp ?: selectedApp.value,
                    onSelectionChanged = { app ->
                        selectedApp.value = app
                        viewModel.makeAction(NewReminderAction.UpdateAction(RemindAction.OpenApp(app)))
                    }
                )
                is RemindAction.OpenUrl -> TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 16.dp, end = 16.dp),
                    value = selectedUrl.value ?: "",
                    singleLine = true,
                    onValueChange = { url ->
                        selectedUrl.value = url
                        //viewModel.makeAction(NewReminderAction.UpdateAction(RemindAction.OpenUrl(url)))
                    },
                    placeholder = { Text("Enter URL") },
                )
            }
        }
        AnimatedVisibility(visible = newReminderState.error == ValidationError.NeedApp) {
            Text(
                text = "Need to pick an app",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        AnimatedVisibility(visible = newReminderState.error == ValidationError.NeedLink) {
            Text(
                text = "You should set a link, to use it",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
        ExtendedFloatingActionButton(
            icon = { Icon(Icons.Filled.Create,"") },
            text = { Text("CREATE REMINDER") },
            backgroundColor = colorResource(id = R.color.purple_700),
            contentColor = colorResource(id = R.color.white),
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = {
                viewModel.makeAction(NewReminderAction.CreateReminder)
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}