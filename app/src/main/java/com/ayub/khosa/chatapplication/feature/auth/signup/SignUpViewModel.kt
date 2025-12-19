package com.ayub.khosa.chatapplication.feature.auth.signup


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.feature.auth.signin.google.AccountService
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val accountService: AccountService) :
    ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()

    fun signUp(name: String, email: String, password: String) {


        _state.value = SignUpState.Loading
        // Firebase signIn
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let {
                        it.updateProfile(
                            com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                        )?.addOnCompleteListener {


                            viewModelScope.launch {

                                try {

                                    signIn(email = email, password = password)
                                    _state.value = SignUpState.Success
                                } catch (e: Exception) {
                                    _state.value = SignUpState.Error
                                    PrintLogs.printD("sendMessage Exception  " + e.message)
                                }


                            }


                        }
                        return@addOnCompleteListener
                    }
                    _state.value = SignUpState.Error

                } else {
                    _state.value = SignUpState.Error
                }
            }
    }

    private fun signIn(email: String, password: String) {
        _state.value = SignUpState.Loading

        viewModelScope.launch {

            try {


                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            PrintLogs.printInfo("  task.isSuccessful  ")
                            task.result.user?.let {
                                _state.value = SignUpState.Success

                                PrintLogs.printInfo("  task.isSuccessful  " + task.result.user?.email)
                                return@addOnCompleteListener
                            }
                            _state.value = SignUpState.Error

                        } else {
                            PrintLogs.printInfo("  task is failed  ")
                            _state.value = SignUpState.Error
                        }
                    }


            } catch (e: Exception) {
                _state.value = SignUpState.Error
                PrintLogs.printD(" Exception  " + e.message)
            }
        }


    }
}

sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    object Error : SignUpState()
}