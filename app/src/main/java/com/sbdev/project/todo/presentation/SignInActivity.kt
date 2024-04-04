package com.sbdev.project.todo.presentation

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.sbdev.project.todo.R
import com.sbdev.project.todo.presentation.viewmodels.AuthViewModel
import com.sbdev.project.todo.ui.theme.ToDoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(authViewModel.userId.isNotEmpty()) {
            goToNextScreen()
        }

        oneTapClient = Identity.getSignInClient(this)

        setContent {
            ToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignInScreen {
                        signInWithGoogle()
                    }
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.googleSignInFlow.collectLatest { userId ->
                userId?.let { _ ->
                    goToNextScreen()
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.web_client_id))
                .build()
        ).build()

        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener { result ->
            try {
                oneTapSignInLauncher.launch(
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                        .setFlags(0, 0)
                        .setFillInIntent(null).build()
                )
                Log.d(TAG, "signInWithGoogle: sign in success")
            } catch (e: IntentSender.SendIntentException) {
                Log.e(TAG, "Can't start One Tap UI: ${e.localizedMessage}")
            }
        }.addOnCanceledListener {
            Log.d(TAG, "Google Sign In cancelled")
        }.addOnFailureListener {
            Log.e(TAG, "signInWithGoogle: failed: ${it.localizedMessage}")
        }
    }

    private var oneTapSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            result.data?.let {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(it)
                    val idToken = credential.googleIdToken

                    idToken?.let { token ->
                        authViewModel.verifyGoogleSignInWithFirebase(token)
                    } ?: run {
                        Log.e(TAG, "No token found")
                    }
                } catch (e: ApiException) {
                    Log.e(TAG, "oneTapSignInLauncher: ${e.localizedMessage}")
                }
            }
        }

    private fun goToNextScreen() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    companion object {
        val TAG: String = SignInActivity::class.java.name
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreen(onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.to_do),
            contentDescription = "",
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(48.dp))

        ElevatedButton(
            onClick = {
                onClick.invoke()
            },
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Sign In with Google")
        }
    }
}