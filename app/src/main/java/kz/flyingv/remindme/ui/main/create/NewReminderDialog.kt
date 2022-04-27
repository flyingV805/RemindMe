package kz.flyingv.remindme.ui.main.create

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kz.flyingv.remindme.data.model.InstalledApp
import kz.flyingv.remindme.data.model.RemindAction
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.ui.statemodel.RemindActionEnum
import kz.flyingv.remindme.ui.statemodel.RemindTypeEnum
import kz.flyingv.remindme.ui.widgets.iconselector.*
import kz.flyingv.remindme.ui.widgets.isInPreview
import kz.flyingv.remindme.ui.widgets.previewNewReminderState
import kz.flyingv.remindme.ui.widgets.selector.SegmentText
import kz.flyingv.remindme.ui.widgets.selector.SegmentedControl

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewReminderDialog(dialogState: ModalBottomSheetState, viewModel: NewReminderViewModel = viewModel()){

    val scope = rememberCoroutineScope()

    val newReminderState = if(!isInPreview()){
        viewModel.newReminderStateFlow.collectAsState().value
    }else{
        previewNewReminderState()
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
    val lastSelectedDayOfWeek = remember{ mutableStateOf(0) }
    val lastSelectedDayOfMonth = remember{ mutableStateOf(0) }
    val daysOfMonthScrollState = remember{ LazyListState() }
    val selectedApp = remember {mutableStateOf<InstalledApp?>(null)}

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
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
            value = newReminderState.name,
            singleLine = true,
            onValueChange = {
                viewModel.makeAction(NewReminderAction.UpdateName(it))
            },
            placeholder = { Text("Reminder Name") },
        )
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
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Yearly(0, 0)))
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
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
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
                    onSelectionChanged = {day, month ->
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Yearly(day, month)))
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
                        viewModel.makeAction(NewReminderAction.UpdateAction(RemindAction.OpenUrl(null)))
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
            modifier = Modifier.fillMaxWidth().height(72.dp),
            targetState = newReminderState.action
        ) {
            when(it){
                is RemindAction.DoNothing ->
                    Box(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
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
                    modifier =Modifier.fillMaxWidth().padding(top = 4.dp, start = 16.dp, end = 16.dp),
                    value = it.url ?: "",
                    singleLine = true,
                    onValueChange = {},
                    placeholder = { Text("Enter URL") },
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
        ExtendedFloatingActionButton(
            icon = { Icon(Icons.Filled.Create,"") },
            text = { Text("CREATE REMINDER") },
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = {
                scope.launch { dialogState.hide() }
                viewModel.makeAction(NewReminderAction.CreateReminder)
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}