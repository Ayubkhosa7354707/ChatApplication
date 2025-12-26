package com.ayub.khosa.chatapplication.viewmodel




import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.utils.Response
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.AuthUseCases
import com.ayub.khosa.chatapplication.utils.PrintLogs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    var isUserSignInState = mutableStateOf(false)
        private set

    var toastMessage = mutableStateOf("")
        private set

    fun signIn(email: String, password: String) {
        viewModelScope.launch {


            authUseCases.signIn(email,password).collect  { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = "Login Failed"
                    }
                    is Response.Success -> {
                        PrintLogs.printInfo("signIn success "+response.data)
                        isUserSignInState.value = response.data
                        toastMessage.value = "Login Successful"
                    }
                    is Response.Error -> {
                        toastMessage.value = "Login Failed"
                    }
                }
            }
        }
    }








}