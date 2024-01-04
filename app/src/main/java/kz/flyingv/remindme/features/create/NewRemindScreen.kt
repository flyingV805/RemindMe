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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.features.create.ui.SegmentedControl

@Composable
fun NewRemindScreen(
    viewModel: NewRemindViewModel = viewModel(),
    onHide: () -> Unit
) {

    val viewState by viewModel.provideState().collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text("CREATE NEW REMINDER", style = MaterialTheme.typography.titleLarge, maxLines = 2)

        //Reminder name
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = viewState.name,
            singleLine = true,
            isError = viewState.error == ValidationError.NeedName,
            onValueChange = { viewModel.reduce(NewRemindAction.UpdateName(it)) },
            placeholder = { Text("Reminder Name") },
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )
        AnimatedVisibility(visible = viewState.error == ValidationError.NeedName) {
            Text(
                text = "You need to name it, trust me",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
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
                    ReminderType.Daily -> {
                        viewModel.reduce(NewRemindAction.UpdateType(ReminderType.Daily))
                    }
                    ReminderType.Weekly -> {
                        viewModel.reduce(NewRemindAction.UpdateType(ReminderType.Weekly(lastSelectedDayOfWeek.value)))
                    }
                    ReminderType.Monthly -> {
                        viewModel.reduce(NewRemindAction.UpdateType(ReminderType.Monthly(lastSelectedDayOfMonth.value)))
                    }
                    ReminderType.Yearly -> {
                        viewModel.reduce(NewRemindAction.UpdateType(ReminderType.Yearly(lastSelectedDayOfMonthInYear.value, lastSelectedMonthOfYear.value)))
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
            targetState = viewState.reminderType,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            label = "",
        ) { remindType ->
            when(remindType){
                is ReminderType.Daily -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Remind every day!")
                }
                is ReminderType.Weekly -> DayOfWeekSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDay = (newReminderState.type as? RemindType.Weekly)?.dayOfWeek ?: lastSelectedDayOfWeek.value,
                    onSelectionChanged = {
                        lastSelectedDayOfWeek.value = it
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Weekly(it)))
                    }
                )
                is ReminderType.Monthly -> DayOfMonthSelector(
                    modifier = Modifier.fillMaxWidth(),
                    scrollState = daysOfMonthScrollState,
                    selectDay = (newReminderState.type as? RemindType.Monthly)?.dayOfMonth ?: lastSelectedDayOfMonth.value,
                    onSelectionChanged = {
                        lastSelectedDayOfMonth.value = it
                        viewModel.makeAction(NewReminderAction.UpdateType(RemindType.Monthly(it)))
                    }
                )
                is ReminderType.Yearly -> DayOfYearSelector(
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
            targetState = viewState.reminderAction,
            label = ""
        ) {
            when(it){
                is ReminderAction.DoNothing ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "OK, no actions")
                    }
                is ReminderAction.OpenApp -> AppSelector(
                    apps = newReminderState.actionApps,
                    selectedApp = it.installedApp ?: selectedApp.value,
                    onSelectionChanged = { app ->
                        selectedApp.value = app
                        viewModel.reduce(NewRemindAction.UpdateAction(RemindAction.OpenApp(app)))
                    }
                )
                is ReminderAction.OpenUrl -> TextField(
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
        AnimatedVisibility(visible = viewState.error == ValidationError.NeedApp) {
            Text(
                text = "Need to pick an app",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        AnimatedVisibility(visible = viewState.error == ValidationError.NeedLink) {
            Text(
                text = "You should set a link, to use it",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }


        Button(
            onClick = { onHide() }
        ) {
            Text("Hide bottom sheet")
        }

    }


}