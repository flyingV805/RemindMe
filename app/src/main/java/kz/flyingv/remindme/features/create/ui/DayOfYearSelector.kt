package kz.flyingv.remindme.features.create.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.flyingv.remindme.domain.entity.MonthOfYear
import kz.flyingv.remindme.utils.datetime.DatetimeUtils

@Composable
fun DayOfYearSelector(
    modifier: Modifier = Modifier,
    selectedDay: Int = 1,
    selectedMonth: MonthOfYear = MonthOfYear.Jan,
    onSelectionChanged: (day: Int, month: MonthOfYear) -> Unit
) {

    var daysInMonthCount = remember { DatetimeUtils.daysInMonth(selectedMonth) }

    val showMonthDropdown = remember{ mutableStateOf(false) }
    val showDayOfMonthDropdown = remember{ mutableStateOf(false) }

    if(selectedDay > daysInMonthCount){
        onSelectionChanged(daysInMonthCount, selectedMonth)
    }

    Row(
        modifier = modifier
            .fillMaxHeight()
            .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ){
        Box(modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .fillMaxHeight()
            .weight(1f)
            .background(MaterialTheme.colors.surface, RoundedCornerShape(8.dp))
            .clickable {
                showMonthDropdown.value = true
            },
            contentAlignment = Alignment.CenterEnd
        ){
            Text(
                text = DatetimeUtils.monthOfYearString(selectedMonth),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "", modifier = Modifier.padding(end = 8.dp))
            DropdownMenu(
                expanded = showMonthDropdown.value,
                onDismissRequest = { showMonthDropdown.value = false }) {
                MonthOfYear.entries.forEach { month ->
                    DropdownMenuItem(onClick = {
                        showMonthDropdown.value = false
                        daysInMonthCount = DatetimeUtils.daysInMonth(month)
                        onSelectionChanged(selectedDay, month)
                    }) {
                        Text(DatetimeUtils.monthOfYearString(month))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .fillMaxHeight()
            .weight(1f)
            .background(MaterialTheme.colors.surface, RoundedCornerShape(8.dp))
            .clickable {
                showDayOfMonthDropdown.value = true
            },
            contentAlignment = Alignment.CenterEnd
        ){
            Text(
                text = (selectedDay).toString(),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "", modifier = Modifier.padding(end = 8.dp))
            DropdownMenu(
                expanded = showDayOfMonthDropdown.value,
                onDismissRequest = { showDayOfMonthDropdown.value = false }) {

                for (day in 1..daysInMonthCount){
                    DropdownMenuItem(onClick = {
                        //selectedDayOfMonthInner = index
                        showDayOfMonthDropdown.value = false
                        onSelectionChanged(day, selectedMonth)
                    }) {
                        Text((day).toString())
                    }
                }

            }
        }

    }

}

@Preview
@Composable fun DayOfYearSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day of Year Selector", style = MaterialTheme.typography.caption)

                DayOfYearSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onSelectionChanged = {day, month ->  }
                )
            }
        }
    }
}
