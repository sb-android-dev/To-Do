package com.sbdev.project.todo.data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreSource {
    val firestore: FirebaseFirestore = Firebase.firestore

    fun createNewDocument(collection: CollectionReference): DocumentReference =
        collection.document()

    fun addToDo(userId: String, toDoItem: ToDoItem): Task<DocumentReference> {
        val hashMap = hashMapOf(
            "title" to toDoItem.title,
            "description" to toDoItem.description,
            "status" to toDoItem.status,
            "time" to Timestamp.now()
        )
        return firestore.collection(userId).add(hashMap)
    }

    fun updateToDo(userId: String, toDoItem: ToDoItem): Task<Void> {
        val hashMap = hashMapOf(
            "title" to toDoItem.title,
            "description" to toDoItem.description,
            "status" to toDoItem.status
        )
        return firestore.collection(userId).document(toDoItem.id).set(hashMap, SetOptions.merge())
    }

    fun deleteToDo(userId: String, toDoId: String): Task<Void> {
        return firestore.collection(userId).document(toDoId).delete()
    }

    fun getToDos(userId: String): Query {
        Log.d("FirestoreSource", "getToDos: $userId")
        val query = firestore.collection(userId).orderBy("time", Query.Direction.DESCENDING)
        return query
    }
}