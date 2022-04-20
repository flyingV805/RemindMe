package kz.flyingv.remindme.activity.main.fragment

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
import kotlinx.coroutines.launch
import kz.flyingv.remindme.model.RemindTypeEnum
import kz.flyingv.remindme.ui.iconselector.DayOfMonthSelector
import kz.flyingv.remindme.ui.iconselector.DayOfWeekSelector
import kz.flyingv.remindme.ui.iconselector.DayOfYearSelector
import kz.flyingv.remindme.ui.iconselector.IconSelector
import kz.flyingv.remindme.ui.isInPreview
import kz.flyingv.remindme.ui.previewNewReminderState
import kz.flyingv.remindme.ui.selector.SegmentText
import kz.flyingv.remindme.ui.selector.SegmentedControl

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewReminderDialog(dialogState: ModalBottomSheetState, viewModel: NewReminderViewModel){

    val scope = rememberCoroutineScope()

    val newReminderState = if(!isInPreview()){
        viewModel.newReminderStateFlow.collectAsState().value
    }else{
        previewNewReminderState()
    }

    val remindTypes = remember { listOf(RemindTypeEnum.Daily, RemindTypeEnum.Weekly, RemindTypeEnum.Monthly, RemindTypeEnum.Yearly) }
    var selectedRemindType by remember { mutableStateOf(RemindTypeEnum.Daily) }
    var selectIcon by remember { mutableStateOf(0) }

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

        Text(text ="VIEW DETAIL", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "VIEW DETAIL", style = MaterialTheme.typography.caption)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text ="VIEW DETAIL", style = MaterialTheme.typography.h6)
        Text(text = "VIEW DETAIL", style = MaterialTheme.typography.caption)
        Text(text ="VIEW DETAIL", style = MaterialTheme.typography.h6)
        Text(text = "VIEW DETAIL", style = MaterialTheme.typography.caption)
        Spacer(modifier = Modifier.height(16.dp))
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