package kz.flyingv.remindme.ui.main.create

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kz.flyingv.remindme.ui.statemodel.RemindActionEnum
import kz.flyingv.remindme.ui.statemodel.RemindTypeEnum
import kz.flyingv.remindme.ui.uicomponents.iconselector.*
import kz.flyingv.remindme.ui.uicomponents.isInPreview
import kz.flyingv.remindme.ui.uicomponents.previewNewReminderState
import kz.flyingv.remindme.ui.uicomponents.selector.SegmentText
import kz.flyingv.remindme.ui.uicomponents.selector.SegmentedControl

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
    var selectedRemindType by remember { mutableStateOf(RemindTypeEnum.Daily) }

    val remindActions = remember { listOf(RemindActionEnum.Nothing, RemindActionEnum.OpenApp, RemindActionEnum.OpenUrl) }

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
            contentAlignment = Alignment.Center
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            contentAlignment = Alignment.Center){

            androidx.compose.animation.AnimatedVisibility(
                enter = fadeIn(), exit = fadeOut(),
                visible = newReminderState.action == RemindActionEnum.Nothing
            ) {

            }

            androidx.compose.animation.AnimatedVisibility(
                enter = fadeIn(), exit = fadeOut(),
                visible = newReminderState.action == RemindActionEnum.OpenApp
            ) {
                AppSelector(
                    apps = newReminderState.actionApps,
                    onSelectionChanged = {}
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                enter = fadeIn(), exit = fadeOut(),
                visible = newReminderState.action == RemindActionEnum.OpenUrl
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    singleLine = true,
                    onValueChange = {

                    },
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