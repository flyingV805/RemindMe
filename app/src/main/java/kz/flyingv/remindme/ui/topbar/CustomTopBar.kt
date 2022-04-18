package kz.flyingv.remindme.ui.topbar

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable fun CustomTopBar(
    modifier: Modifier = Modifier,
    onSearchStarted: () -> Unit,
    onSearchUpdate: (String) -> Unit,
    onSearchClose: () -> Unit,
    isSearching: Boolean = false,
    searchValue: String = ""
) {
    //val state = remember { CustomTopBarState() }
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = modifier
            .padding(top = 8.dp, start = 8.dp, bottom = 8.dp, end = 8.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(4.dp, RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp))
        ){
            BackHandler(enabled = isSearching) {
                onSearchClose()
            }
            AnimatedVisibility(visible = !isSearching, enter = fadeIn(), exit = fadeOut()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Remind ME", style = MaterialTheme.typography.h6)
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = {
                        //focusRequester.requestFocus()
                        onSearchStarted()
                        //viewModel.makeAction(MainAction.StartSearch)
                    }) {
                        Icon(Icons.Filled.Search, "search")
                    }
                }
            }
            AnimatedVisibility(visible = isSearching, enter = fadeIn(), exit = fadeOut()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(modifier = Modifier.width(4.dp))
                    TextField(
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        value = searchValue,
                        placeholder = { Text("Searching for...") },
                        singleLine = true,
                        onValueChange = { newValue -> onSearchUpdate(newValue) },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor =  Color.Transparent, //hide the indicator
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Box(modifier = Modifier.padding(8.dp)){
                        IconButton(onClick = {
                            onSearchClose()
                        }) {
                            Icon(Icons.Filled.Close, "search")
                        }
                    }
                }
            }
        }
    }
}
/*
private class CustomTopBarState {

    var isSearching = false
    var searchValue = ""

}*/

@Preview
@Composable fun CustomTopBarDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Custom Top Bar", style = MaterialTheme.typography.caption)

                CustomTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    onSearchStarted = {},
                    onSearchUpdate = {},
                    onSearchClose = {},
                    isSearching = false,
                    searchValue = "mainState.searchText"
                )
            }
        }
    }
}