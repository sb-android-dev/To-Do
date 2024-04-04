package com.sbdev.project.todo.data

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class ToDoItem(
    @get:PropertyName("title")
    @set:PropertyName("title")
    var title: String = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("status")
    @set:PropertyName("status")
    var status: String = "to_do",

    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = ""
)
