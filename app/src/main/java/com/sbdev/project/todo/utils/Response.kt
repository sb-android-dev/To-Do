package com.sbdev.project.todo.utils

sealed class Response<out R> {
    data class Success<out T>(val data: T? = null, val msg: String? = null) : Response<T>()
    class Error(val msg: String? = null, val e: Exception? = null): Response<Nothing>()
    object Loading: Response<Nothing>()
}
