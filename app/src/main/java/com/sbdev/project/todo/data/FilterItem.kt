package com.sbdev.project.todo.data

data class FilterItem(
    val label: String,
    val status: ToDoStatus? = null
)
