package com.ayub.khosa.chatapplication.screens.home

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.HomeUseCase
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    var isUserSignOutInFirebase = mutableStateOf(false)
        private set



    var toastMessage = mutableStateOf("")
        private set


    init {
        getfcmtoken()
    }

    @SuppressLint("SuspiciousIndentation")
    fun getfcmtoken() {
        PrintLogs.printD("getfcmtoken  ")
        viewModelScope.launch(Dispatchers.IO) {
            try {

                  homeUseCase.getfcmtoken().collect { response ->
                      when(response) {
                          is Response.Loading -> {
                              toastMessage.value = ""

                          }
                          is Response.Success->{
                              PrintLogs.printInfo("getfcmtoken success "+response.data)
                              toastMessage.value = "getfcmtoken Successful"
                          }

                          is Response.Error -> PrintLogs.printE("Error getfcmtoken "+ response.message)
                      }


}
            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }
    }
  private  fun signOut() {
        viewModelScope.launch {
            // first offline the sign out


            homeUseCase.signOut().collect { response ->
                when(response) {
                    is Response.Loading -> {
                        toastMessage.value = ""

                    }
                    is Response.Success<*> -> {
                        PrintLogs.printInfo("signOut success "+response.data)
                        isUserSignOutInFirebase.value= response.data as Boolean

                        toastMessage.value = "Sign Out"
                    }
                    is Response.Error -> PrintLogs.printE("Error signout "+ response.message)
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
if(response.data){
    signOut()
}

                    }
                    is Response.Error -> {}
                }
            }
        }
    }

}