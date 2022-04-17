package kz.flyingv.remindme.ui.iconselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DayOfMonthSelector(
    modifier: Modifier = Modifier,
    days: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"),
    onSelectionChanged: (dayOfMonth: Int) -> Unit
){
    val state by remember { mutableStateOf(DayOfMonthState()) }

    LazyRow (
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ){
        items(
            count = days.count(),
            itemContent = {
                Spacer(modifier = Modifier.width(4.dp))
                DayOfMonthItem(
                    name = days[it],
                    isSelected = state.selectDayIndex == it,
                    onSelect = {
                        state.selectDayIndex = it
                        onSelectionChanged(it)
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        )
    }

}

private class DayOfMonthState {
    var selectDayIndex by mutableStateOf(0)
}

@Composable
fun DayOfMonthItem(
    name: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
){
    Box(modifier = Modifier
        .shadow(4.dp, CircleShape)
        .width(36.dp)
        .aspectRatio(1f)
        .background(
            if (isSelected) {
                Color.Cyan
            } else {
                Color.White
            },
            CircleShape
        )
        .clickable {
            onSelect()
        },
        contentAlignment = Alignment.Center
    ){
        Text(name, modifier = Modifier.padding(4.dp))
        //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
    }
}

@Preview
@Composable
fun DayOfMonthSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day Of Month Selector", style = MaterialTheme.typography.caption)

                DayOfMonthSelector(
                    modifier = Modifier.fillMaxWidth(),
                    onSelectionChanged = {}
                )
            }
        }
    }
}