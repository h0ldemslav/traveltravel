package cz.mendelu.pef.traveltravel.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedCategory: String,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        TextField(
            readOnly = true,
            value = selectedCategory,
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            content()
        }
    }
}
