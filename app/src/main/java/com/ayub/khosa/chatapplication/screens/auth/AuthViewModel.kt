package com.ayub.khosa.chatapplication.screens.auth

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.AuthUseCases
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    var isUserSignInState = mutableStateOf(false)
        private set

    var isUserSignUpState = mutableStateOf(false)
        private set

    var toastMessage = mutableStateOf("")
        private set

    private var fcmtoken = mutableStateOf("")

    init {
        getfcmtoken()
    }


    fun signIn(email: String, password: String) {

        viewModelScope.launch {


            authUseCases.signIn(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }

                    is Response.Success -> {
                        PrintLogs.printInfo("signIn success " + response.data)
                        isUserSignInState.value = response.data
                        if (response.data) {

                            setUserStatusToFirebase(UserStatus.ONLINE)
                        }
                        toastMessage.value = "Login Successful"
                    }

                    is Response.Error -> {
                        toastMessage.value = "Login Failed"
                    }
                }
            }
        }

    }


    fun signUp(email: String, password: String) {


        viewModelScope.launch {


            authUseCases.signUp(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }

                    is Response.Success -> {
                        PrintLogs.printInfo("signIn success " + response.data)
                        isUserSignUpState.value = response.data
                        if (response.data) {

                            setUserStatusToFirebase(UserStatus.ONLINE)
                        }
                        toastMessage.value = "Sign up Successful"
                    }

                    is Response.Error -> {
                        toastMessage.value = "Sign up Failed"
                    }
                }
            }
        }

    }


    private fun setUserStatusToFirebase(userStatus: UserStatus) {

        viewModelScope.launch {
            authUseCases.setUserStatusToFirebase(userStatus).collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {}
                    is Response.Error -> {}
                }
            }
        }

    }

    fun onSignInWithGoogle(credential: Credential) {
        viewModelScope.launch {


            authUseCases.onSignInWithGoogle(credential).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }

                    is Response.Success -> {
                        PrintLogs.printInfo("SignInWithGoogle success " + response.data)
                        isUserSignInState.value = response.data
                        if (response.data) {
                            setUserStatusToFirebase(UserStatus.ONLINE)
                        }
                        toastMessage.value = "SignInWithGoogle Successful"
                    }

                    is Response.Error -> {
                        toastMessage.value = "SignInWithGoogle Failed"
                    }
                }
            }
        }


    }

    @SuppressLint("SuspiciousIndentation")
    fun getfcmtoken() {

        PrintLogs.printD("getfcmtoken  ")

        viewModelScope.launch(Dispatchers.IO) {
            try {

                authUseCases.getfcmtoken().collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            toastMessage.value = ""

                        }

                        is Response.Success -> {
                            PrintLogs.printInfo("getfcmtoken success " + response.data)
                            fcmtoken.value = response.data
                            toastMessage.value = "getfcmtoken Successful"
                        }

                        is Response.Error -> PrintLogs.printE("Error getfcmtoken " + response.message)
                    }


                }
            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }

    }


}