package kz.flyingv.remindme.ui.widgets.selector

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

@Composable
fun DayOfYearSelector(
    modifier: Modifier = Modifier,
    selectedDay: Int = 0,
    selectedMonth: Int = 0,
    onSelectionChanged: (day: Int, month: Int) -> Unit
) {

    val months = remember { listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December") }
    var daysInMonthCount = remember { daysInMonth(selectedMonth + 1) }

    val showMonthDropdown = remember{ mutableStateOf(false) }
    val showDayOfMonthDropdown = remember{ mutableStateOf(false) }

    if(selectedDay > daysInMonthCount - 1){
        onSelectionChanged(daysInMonthCount - 1, selectedMonth)
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
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {
                showMonthDropdown.value = true
            },
            contentAlignment = Alignment.CenterEnd
        ){
            Text(
                text = months[selectedMonth],
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "", modifier = Modifier.padding(end = 8.dp))
            DropdownMenu(
                expanded = showMonthDropdown.value,
                onDismissRequest = { showMonthDropdown.value = false }) {
                months.forEachIndexed { index, month ->
                    DropdownMenuItem(onClick = {
                        //selectedMonthInner.value = index
                        showMonthDropdown.value = false
                        //+1 cause index is not month number
                        daysInMonthCount = daysInMonth(index + 1)
                        onSelectionChanged(selectedDay, index)
                    }) {
                        Text(month)
                    }
                }
            }
            //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .fillMaxHeight()
            .weight(1f)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {
                showDayOfMonthDropdown.value = true
            },
            contentAlignment = Alignment.CenterEnd
        ){
            Text(
                text = (selectedDay + 1).toString(),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "", modifier = Modifier.padding(end = 8.dp))
            DropdownMenu(
                expanded = showDayOfMonthDropdown.value,
                onDismissRequest = { showDayOfMonthDropdown.value = false }) {

                for (index in 0 until daysInMonthCount){
                    DropdownMenuItem(onClick = {
                        //selectedDayOfMonthInner = index
                        showDayOfMonthDropdown.value = false
                        onSelectionChanged(index, selectedMonth)
                    }) {
                        Text((index + 1).toString())
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

fun daysInMonth(month: Int): Int{
    val result = if (month == 4 || month == 6 || month == 9 || month == 11){
        30
    }else if(month == 2){
        28
    }else{
        31
    }
    return result
}