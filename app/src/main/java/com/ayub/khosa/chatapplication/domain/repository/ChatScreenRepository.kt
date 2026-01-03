package com.ayub.khosa.chatapplication.domain.repository

import com.ayub.khosa.chatapplication.domain.model.ChatMessage
import com.ayub.khosa.chatapplication.utils.Response
import kotlinx.coroutines.flow.Flow

interface ChatScreenRepository {
    fun insertMessageToFirebase(
        chatRoomUUID: String,
        messageTitle: String,
        messageContent: String,
        registerUUID: String,
        reciver_fcmtoken: String
    ): Flow<Response<Boolean>>

    fun loadMessageFromFirebase(chatRoomUUID: String): Flow<Response<List<ChatMessage>>>
}