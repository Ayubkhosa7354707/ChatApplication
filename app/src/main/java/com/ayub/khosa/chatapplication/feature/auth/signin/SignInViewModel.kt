package com.ayub.khosa.chatapplication.feature.auth.signin


import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.feature.auth.signin.google.AccountService
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val accountService: AccountService) :
    ViewModel() {

    private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state = _state.asStateFlow()

    fun signIn(email: String, password: String) {
        _state.value = SignInState.Loading

        viewModelScope.launch {

            try {


                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            PrintLogs.printInfo("  task.isSuccessful  ")
                            task.result.user?.let {
                                _state.value = SignInState.Success

                                PrintLogs.printInfo("  task.isSuccessful  " + task.result.user?.email)
                                return@addOnCompleteListener
                            }
                            _state.value = SignInState.Error

                        } else {
                            PrintLogs.printInfo("  task is failed  ")
                            _state.value = SignInState.Error
                        }
                    }
                    .addOnFailureListener { exception ->
                        _state.value = SignInState.Error
                        PrintLogs.printD("signInWithEmail Exception  " + exception.message)
                    }


            } catch (e: Exception) {
                _state.value = SignInState.Error
                PrintLogs.printD("signInWithEmail Exception  " + e.message)
            }
        }


    }


    fun onSignInWithGoogle(credential: Credential) {

        _state.value = SignInState.Loading
        viewModelScope.launch {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                accountService.signInWithGoogle(googleIdTokenCredential.idToken)
                PrintLogs.printInfo(" googleIdToken :" + googleIdTokenCredential.idToken)


                PrintLogs.printInfo("Go to home screen")
                _state.value = SignInState.Success
            } else {
                PrintLogs.printE(" UNEXPECTED_CREDENTIAL")
                _state.value = SignInState.Error
            }
        }
    }
}

sealed class SignInState {


    object Loading : SignInState()
    object Success : SignInState()
    object Error : SignInState()
    object Nothing : SignInState()
}