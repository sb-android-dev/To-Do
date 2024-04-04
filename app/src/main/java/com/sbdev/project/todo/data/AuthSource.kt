package com.sbdev.project.todo.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthSource {
    private val auth: FirebaseAuth = Firebase.auth

    fun getAuthId(): String? = auth.uid

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signInWithGoogle(idToken: String, accessToken: String?): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, accessToken)
        return auth.signInWithCredential(credential)
    }

    fun signOut() {
        auth.signOut()
    }

}