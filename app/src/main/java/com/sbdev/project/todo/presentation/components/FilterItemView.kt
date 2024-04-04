package com.sbdev.project.todo.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sbdev.project.todo.data.FilterItem
import com.sbdev.project.todo.data.ToDoStatus
import com.sbdev.project.todo.ui.theme.pastelGreen
import com.sbdev.project.todo.ui.theme.pastelYellow

@Preview(showBackground = true)
@Composable
fun FilterItemView(
    item: FilterItem = FilterItem(
        label = "Filter name"
    ),
    isSelected: Boolean = false,
    onSelectionChanged: (FilterItem) -> Unit = {}
) {

    FilterChip(
        selected = isSelected,
        onClick = {
            onSelectionChanged.invoke(item)
        },
        label = {
            Text(text = item.label)
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = when(item.status) {
                ToDoStatus.IN_PROGRESS -> pastelYellow
                ToDoStatus.DONE -> pastelGreen
                else -> MaterialTheme.colorScheme.surface
            }
        )
    )
}

