package com.ayub.khosa.chatapplication.screens.userlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.domain.model.User
import com.ayub.khosa.chatapplication.domain.usecase.userslist.UserListScreenUseCases
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userListScreenUseCases: UserListScreenUseCases
) : ViewModel() {

    var serch_user = mutableStateOf(User())
        private set


    var chatRoomId = mutableStateOf("")
        private set

    init {
        PrintLogs.printInfo("UserListViewModel ")
        loadFriendList()
    }


    var loadFriendList = mutableStateOf<List<User>>(listOf())
        private set

    fun loadFriendList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userListScreenUseCases.loadFriendListFromFirebase().collect {response ->
                    when (response) {
                        is Response.Error -> {
                            PrintLogs.printE("loadFriendList Error " + response.message)
                        }

                        is Response.Loading -> {
                            PrintLogs.printD("loadFriendList Loading ")
                        }

                        is Response.Success<*> -> {
                            PrintLogs.printInfo("<---- loadFriendList success ---> ")

                            if (response.data.toString().isNotEmpty()) {
                                loadFriendList.value = response.data as List<User>
                            }


                        }
                    }

                }
            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }
    }

    fun searchUserFromFirebase(email: String) {
        PrintLogs.printD("searchUserFromFirebase  ")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userListScreenUseCases.searchUserFromFirebase(email).collect { response ->
                    when (response) {
                        is Response.Error -> {
                            PrintLogs.printE("searchUserFromFirebase Error " + response.message)
                        }

                        is Response.Loading -> {
                            PrintLogs.printD("searchUserFromFirebase Loading ")
                        }

                        is Response.Success -> {
                            PrintLogs.printInfo("<---- searchUserFromFirebase success ---> ")
                            PrintLogs.printInfo("searchUserFromFirebase success email : " + response.data.userEmail)
                            PrintLogs.printInfo("searchUserFromFirebase success id    : " + response.data.profileUUID)
                            PrintLogs.printInfo("searchUserFromFirebase success Name  :" + response.data.userName)
                            PrintLogs.printInfo("searchUserFromFirebase success fcmToken  :" + response.data.fcmToken)
                            serch_user.value = response.data


                        }
                    }
                }
            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }


    }


  private  fun createChatRoomToFirebase(acceptorUUID: String) {
        PrintLogs.printD("createChatRoomToFirebase  ")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userListScreenUseCases.createChatRoomToFirebase(acceptorUUID).collect { response ->
                    when (response) {
                        is Response.Error -> {
                            PrintLogs.printE("createChatRoomToFirebase Error " + response.message)
                        }

                        is Response.Loading -> {
                            PrintLogs.printD("createChatRoomToFirebase Loading ")
                        }

                        is Response.Success<*> -> {
                            PrintLogs.printInfo("<---- createChatRoomToFirebase success ---> ")
                            PrintLogs.printInfo("createChatRoomToFirebase success  : " + response.data)
                            if (response.data.toString().isNotEmpty()) {
                                chatRoomId.value = response.data.toString()
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }


    }






    fun checkChatRoomExistedFromFirebase(acceptorUUID: String)   {
        PrintLogs.printD("checkChatRoomExistedFromFirebase  ")
        chatRoomId.value =""
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userListScreenUseCases.checkChatRoomExistedFromFirebase(acceptorUUID).collect { response ->
                    when (response) {
                        is Response.Error -> {
                            PrintLogs.printE("checkChatRoomExistedFromFirebase Error " + response.message)
                        }

                        is Response.Loading -> {
                            PrintLogs.printD("checkChatRoomExistedFromFirebase Loading ")
                        }

                        is Response.Success<*> -> {
                            PrintLogs.printInfo("<---- checkChatRoomExistedFromFirebase success ---> ")
                            PrintLogs.printInfo("checkChatRoomExistedFromFirebase success  : " + response.data)
                            if (response.data.toString()== "NO_CHATROOM_IN_FIREBASE_DATABASE") {
                                 createChatRoomToFirebase( acceptorUUID  )
                            }else{
                                chatRoomId.value = response.data.toString()
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }


    }

}