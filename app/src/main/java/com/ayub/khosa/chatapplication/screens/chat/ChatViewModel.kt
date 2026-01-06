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


    var isMessageSent = mutableStateOf(false)
        private set

    var messageSentState: MutableState<String> = mutableStateOf("")
        private set

    var loadchatmessageList: MutableState<List<ChatMessage>> =
        mutableStateOf<List<ChatMessage>>(listOf())
        private set


    init {
        PrintLogs.printInfo("ChatViewModel init")
        loadchatmessageList =        mutableStateOf<List<ChatMessage>>(listOf())
    }

    fun insertMessageToFirebase(
        chatRoomUUID: String,
        messageTitle: String,
        messageContent: String,
        reciver_UUID: String,
        reciver_fcmtoken: String
    ) {
        isMessageSent.value = false
        viewModelScope.launch {
            chatScreenUseCases.insertMessageToFirebase(
                chatRoomUUID,
                messageTitle,
                messageContent,
                reciver_UUID,
                reciver_fcmtoken
            )
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {}
                        is Response.Error -> {
                            PrintLogs.printE("insertMessageToFirebase ---> " + response.message)
                            messageSentState.value = response.message.toString()
                            isMessageSent.value=true
                        }

                        is Response.Success<*> -> {
                            PrintLogs.printInfo("insertMessageToFirebase ---> " + response.data as Boolean)
                            if (response.data) {
                                messageSentState.value = "Message Sent"
                                isMessageSent.value =true
                            }
                        }
                    }
                }
        }
    }

    fun loadAllchatmessages(chatRoomUUID: String) {

        PrintLogs.printInfo("loadAllchatmessages  Chatview model " + chatRoomUUID)
        loadchatmessageList.value = listOf()
        viewModelScope.launch {

            chatScreenUseCases.loadMessageFromFirebase(chatRoomUUID = chatRoomUUID)
                .collect { response ->
                    when (response) {
                        is Response.Loading -> {}
                        is Response.Error -> {
                            PrintLogs.printE("loadchatmessage ---> " + response.message)
                        }

                        is Response.Success<*> -> {
                            PrintLogs.printInfo("loadchatmessage ---> " + response.data)
                            if (response.data.toString().isNotEmpty()) {

                                loadchatmessageList.value = response.data as List<ChatMessage>

                            }
                        }
                    }

                }

        }
    }
}