package com.sbdev.project.todo.domain

import com.google.firebase.auth.FirebaseUser
import com.sbdev.project.todo.utils.Response

interface AuthRepository {
    fun getAuthId(): String
    fun getCurrentUser(): FirebaseUser?
    fun verifyGoogleUser(idToken: String, accessToken: String? = null, b: ((Response<String>) -> Unit))
    fun signOutUser()
}