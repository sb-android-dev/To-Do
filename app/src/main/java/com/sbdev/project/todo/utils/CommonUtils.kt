package com.sbdev.project.todo.utils

import com.sbdev.project.todo.data.FilterItem
import com.sbdev.project.todo.data.ToDoItem
import com.sbdev.project.todo.data.ToDoStatus

fun getFilterList(includeAll: Boolean = true): List<FilterItem> {
    return mutableListOf<FilterItem>().apply {
        if(includeAll)
            add(FilterItem(label = "All"))

        add(FilterItem(label = "To Do", status = ToDoStatus.TO_DO))
        add(FilterItem(label = "In Progress", status = ToDoStatus.IN_PROGRESS))
        add(FilterItem(label = "Done", status = ToDoStatus.DONE))
    }
}

fun getToDoItems(status: ToDoStatus? = null): List<ToDoItem> {
    val list = listOf(
        ToDoItem(
            title = "To Do Item 1",
            description = "To Do Item description",
            status = "to_do"
        ),
        ToDoItem(
            title = "To Do Item 2",
            description = "To Do Item description",
            status = "in_progress"
        ),
        ToDoItem(
            title = "To Do Item 3",
            description = "To Do Item description",
            status = "done"
        ),
        ToDoItem(
            title = "To Do Item 4",
            description = "To Do Item description",
            status = "to_do"
        ),
        ToDoItem(
            title = "To Do Item 5",
            description = "To Do Item description",
            status = "done"
        ),
        ToDoItem(
            title = "To Do Item 6",
            description = "To Do Item description",
            status = "to_do"
        ),
        ToDoItem(
            title = "To Do Item 7",
            description = "To Do Item description",
            status = "in_progress"
        ),
        ToDoItem(
            title = "To Do Item 8",
            description = "To Do Item description",
            status = "to_do"
        ),
        ToDoItem(
            title = "To Do Item 9",
            description = "To Do Item description",
            status = "done"
        ),
        ToDoItem(
            title = "To Do Item 10",
            description = "To Do Item description",
            status = "to_do"
        ),
        ToDoItem(
            title = "To Do Item 11",
            description = "To Do Item description",
            status = "in_progress"
        ),
        ToDoItem(
            title = "To Do Item 12",
            description = "To Do Item description",
            status = "to_do"
        ),
        ToDoItem(
            title = "To Do Item 13",
            description = "To Do Item description",
            status = "done"
        ),
        ToDoItem(
            title = "To Do Item 14",
            description = "To Do Item description",
            status = "in_progress"
        ),
        ToDoItem(
            title = "To Do Item 15",
            description = "To Do Item description",
            status = "to_do"
        )
    )

    return if (status == null) list else list.filter { it.status == status.value }
}