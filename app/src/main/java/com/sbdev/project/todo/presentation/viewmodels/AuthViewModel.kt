package com.sbdev.project.todo.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbdev.project.todo.domain.AuthRepository
import com.sbdev.project.todo.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val userId = authRepository.getAuthId()

    val currentUser = authRepository.getCurrentUser()

    private val _googleSignInFlow = MutableStateFlow<String?>(null)
    val googleSignInFlow = _googleSignInFlow.asStateFlow()
    fun verifyGoogleSignInWithFirebase(idToken: String) {
        authRepository.verifyGoogleUser(idToken) { response ->
            when (response) {
                is Response.Success -> {
                    viewModelScope.launch {
                        _googleSignInFlow.emit(response.data)
                    }
                }

                is Response.Error -> {
                    Log.d(TAG, "verifyGoogleSignInWithFirebase: onFailure: ${response.msg}")
                    viewModelScope.launch {
                        _googleSignInFlow.emit(null)
                    }
                }

                Response.Loading -> {}
            }
        }
    }

    fun signOut() {
        authRepository.signOutUser()
    }

    companion object {
        val TAG: String = AuthViewModel::class.java.name
    }
}