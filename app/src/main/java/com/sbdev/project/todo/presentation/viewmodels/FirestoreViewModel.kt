package com.sbdev.project.todo.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.toObject
import com.sbdev.project.todo.data.ToDoItem
import com.sbdev.project.todo.domain.AuthRepository
import com.sbdev.project.todo.domain.FirestoreRepository
import com.sbdev.project.todo.utils.Response
import com.sbdev.project.todo.utils.addNewItem
import com.sbdev.project.todo.utils.removeItem
import com.sbdev.project.todo.utils.updateItemAt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirestoreViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
): ViewModel() {

    private val _toDoList = MutableLiveData<List<ToDoItem>>()
    val toDoList: LiveData<List<ToDoItem>> get() = _toDoList

    fun getAllToDos() {
        firestoreRepository.getToDos(authRepository.getAuthId()) { response ->
            when(response) {
                is Response.Success -> {
                    response.data?.let { snapshot ->
                        for(change in snapshot.documentChanges) {
                            val doc = change.document
                            val toDoData = doc.toObject<ToDoItem>()
                            toDoData.id = doc.id

                            when(change.type) {
                                DocumentChange.Type.ADDED,
                                DocumentChange.Type.MODIFIED -> {

                                    val existedToDo = _toDoList.value?.find {
                                        it.id == doc.id
                                    }

                                    existedToDo?.let {
                                        val index = _toDoList.value!!.indexOf(it)
                                        _toDoList.updateItemAt(index, toDoData)
                                    } ?: run {
                                        _toDoList.addNewItem(toDoData)
                                    }
                                }
                                DocumentChange.Type.REMOVED -> {
                                    val existedToDo = _toDoList.value?.find {
                                        it.id == doc.id
                                    }

                                    existedToDo?.let {
                                        _toDoList.removeItem(it)
                                    }
                                }
                            }
                        }
                    }
                }
                is Response.Error -> {
                    Log.e("FirestoreViewModel", "getAllToDos: ${response.msg}")
                }
                Response.Loading -> {

                }
            }
        }
    }

    fun addToDo(toDoItem: ToDoItem) {
        firestoreRepository.addToDo(authRepository.getAuthId(), toDoItem) { response ->
            when(response) {
                is Response.Success -> {
                    Log.d("FirestoreViewModel", "addToDo: ${response.data}")
                }
                is Response.Error -> {
                    Log.d("FirestoreViewModel", "addToDo: ${response.msg}")
                }
                Response.Loading -> {}
            }
        }
    }

    fun updateToDo(toDoItem: ToDoItem) {
        firestoreRepository.updateToDo(authRepository.getAuthId(), toDoItem) { response ->
            when(response) {
                is Response.Success -> {
                    Log.d("FirestoreViewModel", "updateToDo: ${response.data}")
                }
                is Response.Error -> {
                    Log.d("FirestoreViewModel", "updateToDo: ${response.msg}")
                }
                Response.Loading -> {}
            }
        }
    }

    fun deleteToDo(toDoItem: ToDoItem) {
        firestoreRepository.deleteToDo(authRepository.getAuthId(), toDoItem.id) { response ->
            when(response) {
                is Response.Success -> {
                    Log.d("FirestoreViewModel", "deleteToDo: ${response.data}")
                }
                is Response.Error -> {
                    Log.d("FirestoreViewModel", "deleteToDo: ${response.msg}")
                }
                Response.Loading -> {}
            }
        }
    }
}