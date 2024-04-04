package com.sbdev.project.todo.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbdev.project.todo.data.FilterItem
import com.sbdev.project.todo.data.ToDoItem
import com.sbdev.project.todo.data.ToDoStatus
import com.sbdev.project.todo.utils.getFilterList

@Preview(showBackground = true)
@Composable
fun AddToDoScreen(
    onSave: (ToDoItem) -> Unit = {},
    toDoItem: ToDoItem = ToDoItem()
) {
    val filterList = getFilterList(includeAll = false)
    var title by remember {
        mutableStateOf(toDoItem.title)
    }
    var description by remember {
        mutableStateOf(toDoItem.description)
    }
    var selectedFilter by remember {
        mutableStateOf(filterList.find { it.status!!.value == toDoItem.status }
            ?: FilterItem("To Do", ToDoStatus.TO_DO))
    }
    var titleError by remember {
        mutableStateOf(false)
    }
    var descriptionError by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (toDoItem.id.isEmpty()) "Add Task" else "Edit Task",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                if (it.isNotEmpty()) titleError = false
            },
            isError = titleError,
            placeholder = { Text(text = "Title") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            supportingText = {
                if(titleError)
                    Text(text = "Please add title")
            },
            trailingIcon = {
                if(titleError)
                    Icon(imageVector = Icons.Rounded.Info, contentDescription = "error")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                if (it.isNotEmpty()) descriptionError = false
            },
            isError = descriptionError,
            placeholder = { Text(text = "Description") },
            singleLine = false,
            minLines = 3,
            maxLines = 5,
            supportingText = {
                if(descriptionError)
                    Text(text = "Please add description")
            },
            trailingIcon = {
                if(descriptionError)
                    Icon(imageVector = Icons.Rounded.Info, contentDescription = "error")
            }
        )

        FilterGroupView(
            list = filterList,
            selectedFilter = selectedFilter,
            onSelectedChanged = {
                selectedFilter = it
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = {
                if (title.isEmpty() || description.isEmpty()) {
                    if (title.isEmpty()) {
                        titleError = true
                    }

                    if (description.isEmpty()) {
                        descriptionError = true
                    }

                    return@ElevatedButton
                }

                val newToDo = ToDoItem(
                    title = title,
                    description = description,
                    status = selectedFilter.status!!.value,
                    id = toDoItem.id
                )
                onSave.invoke(newToDo)
            },
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
        ) {
            Text(text = "Save")
        }
    }
}