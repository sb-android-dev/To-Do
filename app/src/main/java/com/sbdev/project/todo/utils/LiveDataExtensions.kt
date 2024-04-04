package com.sbdev.project.todo.utils

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<List<T>>.addNewItem(item: T) {
    val newList = mutableListOf<T>()
    this.value?.let {
        newList.addAll(it)
    }
    newList.add(item)
    this.value = newList
}

fun <T> MutableLiveData<List<T>>.updateItemAt(index: Int, item: T) {
    val newList = mutableListOf<T>()
    this.value?.let {
        newList.addAll(it)
    }
    newList[index] = item
    this.value = newList
}

fun <T> MutableLiveData<List<T>>.removeItem(item: T) {
    val newList = mutableListOf<T>()
    this.value?.let {
        newList.addAll(it)
    }
    newList.remove(item)
    this.value = newList
}