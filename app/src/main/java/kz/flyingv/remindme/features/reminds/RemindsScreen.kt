package kz.flyingv.remindme.features.reminds

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import kz.flyingv.remindme.R
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.features.create.NewRemindScreen
import kz.flyingv.remindme.features.create.ui.getIcon
import kz.flyingv.remindme.features.profile.ProfileDialog
import kz.flyingv.remindme.features.reminds.dialog.AskPermissionDialog
import kz.flyingv.remindme.features.reminds.dialog.DeleteRemind
import kz.flyingv.remindme.features.reminds.uidata.RemindFormatter
import kz.flyingv.remindme.features.remindtime.RemindTimeDialog
import kz.flyingv.remindme.ui.DancingScript
import kz.flyingv.remindme.utils.datetime.DatetimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindsScreen(viewModel: RemindsViewModel = viewModel()) {

    val uiState by viewModel.provideState().collectAsStateWithLifecycle()
    val listState = viewModel.remindersPaged.collectAsLazyPagingItems()

    val focusRequester = remember { FocusRequester() }
    val focusSearchField by remember { derivedStateOf { uiState.searching } }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)),
                title = {
                    Crossfade(
                        targetState = uiState.searching,
                        label = "searching"
                    ) { showSearch ->
                        when(showSearch){
                            true -> Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Spacer(modifier = Modifier.width(4.dp))
                                TextField(
                                    value = uiState.searchString,
                                    onValueChange = { viewModel.reduce(RemindsAction.Search(it)) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(focusRequester),
                                    singleLine = true,
                                    placeholder = { Text("Searching for...") },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor =  Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                                Box(modifier = Modifier.padding(8.dp)){
                                   IconButton(
                                       onClick = {viewModel.reduce(RemindsAction.EndSearch)}
                                   ) {
                                        Icon(Icons.Filled.Close, "search")
                                    }
                                }
                            }
                            false -> Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Remind Me",
                                    fontFamily = DancingScript,
                                    style = MaterialTheme.typography.headlineLarge.copy(Color.White),
                                )
                                Spacer(Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        viewModel.reduce(RemindsAction.StartSearch)
                                    }
                                ) {
                                    Icon(Icons.Filled.Search, "search")
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)),
                actions = {
                    IconButton(
                        onClick = {  viewModel.reduce(RemindsAction.ShowReminderTime) }
                    ) {
                        Icon(painterResource(id = R.drawable.ic_baseline_alarm_24), contentDescription = "Localized description")
                    }

                    IconButton(
                        onClick = {
                            viewModel.reduce(RemindsAction.ShowProfile)
                        }
                    ) {

                        if(uiState.authorized){
                            Image(
                                painter = rememberAsyncImagePainter(uiState.avatarUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(painterResource(id = R.drawable.baseline_person_add_24), contentDescription = "Localized description")
                        }

                    }

                    if(uiState.syncInProgress){
                        IconButton(
                            onClick = {
                                /* do something */
                            }
                        ){
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        }
                    }

                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { viewModel.reduce(RemindsAction.ShowNewReminder) }
                    ) {
                        Icon(Icons.Filled.Add, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "CREATE")
                    }
                }
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
        ) {

            if(listState.itemCount == 0 || (uiState.searching &&  uiState.searchReminds.isEmpty()) ) {
                item{
                    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie-no-data.json"))

                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Spacer(modifier = Modifier.height(36.dp))
                        Text(
                            text = if(!uiState.searching){
                                "Oops, you don't have any reminders yet..."
                            }else{
                                "Oops, can't find anything..."
                            },
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        LottieAnimation(
                            composition = composition
                        )
                        Text(
                            text = if(!uiState.searching){"Create one ↓"}else{""},
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if(!uiState.searching) {
                items(listState.itemCount){
                    RemindItem(
                        reminder = listState[it],
                        deleteReminder = {reminder ->
                            viewModel.reduce(RemindsAction.AskForDelete(reminder))
                        }
                    )
                }
            } else {
                items(uiState.searchReminds){
                    RemindItem(
                        reminder = it,
                        deleteReminder = {reminder ->
                            viewModel.reduce(RemindsAction.AskForDelete(reminder))
                        }
                    )
                }
            }

            item {
                Box(modifier = Modifier.height(16.dp))
            }

        }

    }


    //ask for permissions
    if (uiState.askNotificationPermissions){
        AskPermissionDialog(hide = {
            viewModel.reduce(RemindsAction.HidePermissionDialog)
        })
    }

    //delete reminder
    uiState.reminderForDelete?.let {reminder ->
        DeleteRemind(
            reminder,
            delete = {
                viewModel.reduce(RemindsAction.Delete(it))
            },
            cancel = {
                viewModel.reduce(RemindsAction.CancelDelete)
            }
        )
    }

    //change remind time
    if (uiState.showRemindTime) {
        RemindTimeDialog(
            hide = {
                viewModel.reduce(RemindsAction.HideReminderTime)
            }
        )
    }

    //add new reminder
    if (uiState.showNewReminder) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.reduce(RemindsAction.HideNewReminder) },
            sheetState = sheetState
        ) {

            NewRemindScreen(
                onHide = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            viewModel.reduce(RemindsAction.HideNewReminder)
                        }
                    }
                }
            )

        }
    }

    //show auth dialog
    if (uiState.showProfileDialog){
        ProfileDialog(
            hide = { viewModel.reduce(RemindsAction.HideProfile) }
        )
    }

    LaunchedEffect(focusSearchField){
        if(focusSearchField) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(Unit){
        viewModel.reduce(RemindsAction.CheckPermissions)
    }

}


@Composable
fun RemindItem(reminder: Reminder?, deleteReminder: (reminder: Reminder) -> Unit?) {
    reminder ?: return Box {}

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable {
                deleteReminder(reminder)
            },
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = getIcon(icon = reminder.icon),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(12.dp)
                        .width(28.dp)
                        .height(28.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(text = reminder.name, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(text = RemindFormatter.formatRemindType(reminder.type), style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Last remind: ${DatetimeUtils.lastTimeDisplayed(reminder.lastShow)}", style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = RemindFormatter.formatRemindAction(reminder.action), style = MaterialTheme.typography.bodySmall, maxLines = 1)
            }
            IconButton(
                onClick = {
                    deleteReminder(reminder)
                }
            ) {
                Icon(Icons.Filled.Delete, "delete", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview
@Composable
fun RemindsScreenPreview(){
    RemindsScreen()
}