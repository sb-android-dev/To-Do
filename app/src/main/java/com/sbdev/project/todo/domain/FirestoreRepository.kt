package com.sbdev.project.todo.domain

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sbdev.project.todo.data.ToDoItem
import com.sbdev.project.todo.utils.Response

interface FirestoreRepository {

    fun getFirestore(): FirebaseFirestore
    fun createNewDocument(collection: CollectionReference): DocumentReference
    fun addToDo(userId: String, toDoItem: ToDoItem, b: (Response<String>) -> Unit)
    fun updateToDo(userId: String, toDoItem: ToDoItem, b: (Response<String>) -> Unit)
    fun deleteToDo(userId: String, toDoId: String, b: (Response<String>) -> Unit)
    fun getToDos(userId: String, b: (Response<QuerySnapshot>) -> Unit)
}