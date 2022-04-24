package kz.flyingv.remindme.ui.uicomponents.iconselector

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
import kz.flyingv.remindme.data.model.InstalledApp


@Composable
fun AppSelector(
    modifier: Modifier = Modifier,
    apps: List<InstalledApp> = emptyList(),
    onSelectionChanged: (app: InstalledApp) -> Unit
) {

    val isDropdownExpanded = remember { mutableStateOf(false)}

    Row(
        modifier = modifier.padding(16.dp)
    ){
        Box(modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .weight(1f)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {
                isDropdownExpanded.value = true
            },
            contentAlignment = Alignment.CenterEnd
        ){
            Text("January", modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), textAlign = TextAlign.Center)
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "", modifier = Modifier.padding(end = 8.dp))

            DropdownMenu(expanded = isDropdownExpanded.value, onDismissRequest = {
                isDropdownExpanded.value = false
            }) {
                apps.forEach {
                    DropdownMenuItem(onClick = {
                        isDropdownExpanded.value = false
                    }) {
                        Text(it.name)
                    }
                }
            }
            //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
        }
    }

}

@Preview
@Composable
fun AppSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day of Year Selector", style = MaterialTheme.typography.caption)

                AppSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onSelectionChanged = {app ->  }
                )
            }
        }
    }
}