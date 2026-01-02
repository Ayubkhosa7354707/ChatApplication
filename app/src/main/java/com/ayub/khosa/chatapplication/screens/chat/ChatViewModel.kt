package com.ayub.khosa.chatapplication.screens.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.domain.model.ChatMessage
import com.ayub.khosa.chatapplication.domain.usecase.chatScreen.ChatScreenUseCases
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatScreenUseCases: ChatScreenUseCases
) : ViewModel() {

    var messageSentState: MutableState<String> = mutableStateOf("")
        private set

    var loadchatmessageList: MutableState<List<ChatMessage>> = mutableStateOf<List<ChatMessage>>(listOf())
        private set


    init {
        PrintLogs.printInfo("ChatViewModel init")
    }
    fun insertMessageToFirebase(
        chatRoomUUID: String,
        messageTitle: String,
        messageContent: String,
        registerUUID: String,
        reciver_fcmtoken: String
    ) {
        messageSentState.value =""
        viewModelScope.launch {
            chatScreenUseCases.insertMessageToFirebase(chatRoomUUID,messageTitle, messageContent, registerUUID ,reciver_fcmtoken)
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {}
                        is Response.Error -> {
                            PrintLogs.printE("insertMessageToFirebase ---> " + response.message)
                            messageSentState.value =response.message.toString()
                        }
                        is Response.Success<*> -> {
                            PrintLogs.printInfo("insertMessageToFirebase ---> " + response.data as Boolean)
                            if(response.data){
                                messageSentState.value ="OK message Sent"
                            }
                        }
                    }
                }
        }
    }

    fun loadAllchatmessages(chatRoomUUID :String, registerUUID: String )   {

        PrintLogs.printInfo("loadAllchatmessages  Chatview model "+chatRoomUUID+"  "+registerUUID)
        loadchatmessageList.value = listOf()
         viewModelScope.launch {

            chatScreenUseCases.loadMessageFromFirebase(chatRoomUUID = chatRoomUUID, registerUUID = registerUUID).collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Error -> {
                        PrintLogs.printE("loadchatmessage ---> " + response.message)
                    }
                    is Response.Success<*> -> {
                        PrintLogs.printInfo("loadchatmessage ---> " + response.data)
                        if(response.data.toString().isNotEmpty()){
                            loadchatmessageList.value=  response.data as List<ChatMessage>
                        }
                    }
                }

            }

        }
    }
}