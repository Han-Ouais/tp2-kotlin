package com.arguvio.tp2Kotlin.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arguvio.tp2Kotlin.ui.activities.ui.theme.MyApplicationTheme
import com.example.myapplication.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class SignInActivity : ComponentActivity() {

    private val signIn: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract(), this::onSignInResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(Firebase.auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.mipmap.ic_launcher)
            .setAvailableProviders(listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.AnonymousBuilder().build()
            ))
            .build()

        signIn.launch(signInIntent)
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if(result.resultCode == RESULT_OK){
            Log.d(TAG, "Sign in successful!")
            return
        }

        Toast.makeText(
            this,
            "There was an error signing in",
            Toast.LENGTH_LONG
        ).show()

        val response = result.idpResponse
        if(response == null){
            Log.w(TAG, "Sign in canceled")
        } else {
            Log.w(TAG, "Sign in error", response.error)
        }
    }

    companion object {
        private const val TAG = "SignInActivity"
    }
}

