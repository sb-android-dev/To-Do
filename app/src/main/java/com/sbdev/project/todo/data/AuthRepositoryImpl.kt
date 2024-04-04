package com.sbdev.project.todo.data

import com.google.firebase.auth.FirebaseUser
import com.sbdev.project.todo.domain.AuthRepository
import com.sbdev.project.todo.utils.Response

class AuthRepositoryImpl(private val authSource: AuthSource) : AuthRepository {

    override fun getAuthId(): String = authSource.getAuthId() ?: ""

    override fun getCurrentUser(): FirebaseUser? = authSource.getCurrentUser()

    override fun verifyGoogleUser(
        idToken: String,
        accessToken: String?,
        b: (Response<String>) -> Unit
    ) {
        authSource.signInWithGoogle(idToken, accessToken)
            .addOnSuccessListener {
                b.invoke(Response.Success(it.user?.uid))
            }.addOnCanceledListener {
                b.invoke(Response.Error("Sign-in is cancelled"))
            }.addOnFailureListener {
                b.invoke(Response.Error(it.message))
            }
    }

    override fun signOutUser() = authSource.signOut()
}