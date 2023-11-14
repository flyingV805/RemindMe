package kz.flyingv.remindme.ui.widget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ReminderAppBar(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF1976D2),
    isSearching: Boolean = false,
    searchValue: String = "",
    onSearchStarted: () -> Unit,
    onSearchUpdate: (String) -> Unit,
    onSearchClosed: () -> Unit,

) {

    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .background(color = color, shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
            .padding(top = 8.dp, start = 8.dp, bottom = 8.dp, end = 8.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(4.dp, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
        ){

            BackHandler(enabled = isSearching) {
                onSearchClosed()
            }

            Crossfade(
                targetState = isSearching,
                label = "searching"
            ) { showSearch ->
                when(showSearch){
                    true -> Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
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

                                onSearchClosed()
                            }) {
                                Icon(Icons.Filled.Close, "search")
                            }
                        }
                    }
                    false -> Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Spacer(modifier = Modifier.width(4.dp))
                        androidx.compose.material.Text("Remind ME", style = androidx.compose.material.MaterialTheme.typography.h6)
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = {
                            onSearchStarted()
                        }) {
                            Icon(Icons.Filled.Search, "search")
                        }
                    }
                }
            }

        }
    }

}


@Preview
@Composable
fun ReminderAppBarPreview() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Custom Top Bar", style = MaterialTheme.typography.bodyMedium)
                ReminderAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    onSearchStarted = {},
                    onSearchUpdate = {},
                    onSearchClosed = {}
                )
            }
        }
    }
}