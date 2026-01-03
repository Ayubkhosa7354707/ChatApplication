package com.ayub.khosa.chatapplication.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.domain.model.User
import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.HomeUseCase
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    init {
        getUserFirebase()
    }

    var isUserSignOutInFirebase = mutableStateOf(false)
        private set


    var myUser: MutableState<User> = mutableStateOf(User())
        private set

    var toastMessage = mutableStateOf("")
        private set


    private fun signOut() {
        viewModelScope.launch {
            // first offline the sign ou

            homeUseCase.signOut().collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""

                    }

                    is Response.Success<*> -> {
                        PrintLogs.printInfo("signOut success " + response.data)
                        isUserSignOutInFirebase.value = response.data as Boolean

                        toastMessage.value = "Sign Out"
                    }

                    is Response.Error -> PrintLogs.printE("Error signout " + response.message)
                }

            }
        }
    }

    fun setUserStatusToFirebase(userStatus: UserStatus) {
        viewModelScope.launch {
            homeUseCase.setUserStatusToFirebase(userStatus).collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if (response.data) {
                            signOut()
                        }
                    }

                    is Response.Error -> {}
                }
            }
        }
    }

    private fun getUserFirebase() {
        viewModelScope.launch {
            homeUseCase.getUserFirebase().collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        myUser.value = response.data
                    }

                    is Response.Error -> {}
                }
            }
        }
    }

}