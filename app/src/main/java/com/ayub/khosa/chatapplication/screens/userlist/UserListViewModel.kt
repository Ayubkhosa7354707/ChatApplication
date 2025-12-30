package com.ayub.khosa.chatapplication.screens.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.HomeUseCase
import com.ayub.khosa.chatapplication.domain.usecase.userslist.UserListScreenUseCases
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class UserListViewModel   @Inject constructor(
    private val userListScreenUseCases: UserListScreenUseCases
) : ViewModel() {



    fun searchUserFromFirebase(email: String) {
        PrintLogs.printD("searchUserFromFirebase  ")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userListScreenUseCases.searchUserFromFirebase(email).collect { response ->
                    when(response) {
                        is Response.Error -> {
                            PrintLogs.printE("searchUserFromFirebase Error "+response.message)
                        }
                       is Response.Loading -> {
                            PrintLogs.printD("searchUserFromFirebase Loading ")
                        }
                        is Response.Success->{
                            PrintLogs.printInfo("<---- searchUserFromFirebase success ---> ")
                            PrintLogs.printInfo("searchUserFromFirebase success email : "+response.data.email)
                            PrintLogs.printInfo("searchUserFromFirebase success id    : "+response.data.uid)
                            PrintLogs.printInfo("searchUserFromFirebase success Name  :"+response.data.displayName)


                        }
                    }
                }
            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }
    }



}