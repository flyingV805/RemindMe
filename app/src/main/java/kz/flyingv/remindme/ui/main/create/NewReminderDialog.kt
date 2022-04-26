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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text ="NEW REMINDER", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))
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
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = newReminderState.name,
            singleLine = true,
            onValueChange = {
                viewModel.makeAction(NewReminderAction.UpdateName(it))
            },
            placeholder = { Text("Reminder Name") },
        )
        Spacer(modifier = Modifier.height(16.dp))
        SegmentedControl(
            remindTypes,
            remindTypeEnum,
            onSegmentSelected = {
                when(it){
                    RemindTypeEnum.Daily -> {viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Daily))}
                    RemindTypeEnum.Weekly -> {viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Weekly(0)))}
                    RemindTypeEnum.Monthly -> {viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Monthly(0)))}
                    RemindTypeEnum.Yearly -> {viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Yearly(0)))}
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

        val daysOfMonthScrollState = remember{ LazyListState() }

        Crossfade(
            targetState = newReminderState.type,
            modifier = Modifier.fillMaxWidth().height(64.dp),
        ) { remindType ->
            when(remindType){
                is RemindType.Daily -> Text("Remind every day")
                is RemindType.Weekly -> DayOfWeekSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDay = (newReminderState.type as? RemindType.Weekly)?.dayOfWeek ?: 0,
                    onSelectionChanged = {
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Weekly(it)))
                    }
                )
                is RemindType.Monthly -> DayOfMonthSelector(
                    modifier = Modifier.fillMaxWidth(),
                    scrollState = daysOfMonthScrollState,
                    selectDay = (newReminderState.type as? RemindType.Monthly)?.dayOfMonth ?: 0,
                    onSelectionChanged = {
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Monthly(it)))
                    }
                )
                is RemindType.Yearly -> DayOfYearSelector(
                    modifier = Modifier.fillMaxWidth(),
                    onSelectionChanged = {day, month ->  }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        SegmentedControl(
            remindActions,
            newReminderState.action,
            onSegmentSelected = { viewModel.makeAction(NewReminderAction.UpdateAction(it)) }
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
                .height(64.dp),
            targetState = newReminderState.action
        ) {
            when(it){
                RemindActionEnum.Nothing -> Text("Do nothing")
                RemindActionEnum.OpenApp -> AppSelector(
                    apps = newReminderState.actionApps,
                    onSelectionChanged = {}
                )
                RemindActionEnum.OpenUrl -> TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    singleLine = true,
                    onValueChange = {},
                    placeholder = { Text("Enter URL") },
                )
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
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