package com.sbdev.project.todo.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sbdev.project.todo.domain.FirestoreRepository
import com.sbdev.project.todo.utils.Response

class FirestoreRepositoryImpl(private val firestoreSource: FirestoreSource) : FirestoreRepository {

    override fun getFirestore(): FirebaseFirestore = firestoreSource.firestore

    override fun createNewDocument(collection: CollectionReference): DocumentReference =
        firestoreSource.createNewDocument(collection)

    override fun addToDo(userId: String, toDoItem: ToDoItem, b: (Response<String>) -> Unit) {
        firestoreSource.addToDo(userId, toDoItem).addOnSuccessListener {
            b.invoke(Response.Success("To-Do added"))
        }.addOnFailureListener {
            b.invoke(Response.Error("Can not add to-do", it))
        }
    }

    override fun updateToDo(userId: String, toDoItem: ToDoItem, b: (Response<String>) -> Unit) {
        firestoreSource.updateToDo(userId, toDoItem).addOnSuccessListener {
            b.invoke(Response.Success("To-Do updated"))
        }.addOnFailureListener {
            b.invoke(Response.Error("Can not update to-do", it))
        }
    }

    override fun deleteToDo(userId: String, toDoId: String, b: (Response<String>) -> Unit) {
        firestoreSource.deleteToDo(userId, toDoId).addOnSuccessListener {
            b.invoke(Response.Success("To-Do deleted"))
        }.addOnFailureListener {
            b.invoke(Response.Error("can not delete to-do", it))
        }
    }

    override fun getToDos(userId: String, b: (Response<QuerySnapshot>) -> Unit) {
        firestoreSource.getToDos(userId).addSnapshotListener { querySnapshot, error ->
            if(error != null) {
                b.invoke(Response.Error(error.message))
                return@addSnapshotListener
            }

            querySnapshot?.let {
                b.invoke(Response.Success(it))
            }
        }
    }
}