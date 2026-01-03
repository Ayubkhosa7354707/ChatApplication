package com.ayub.khosa.chatapplication.domain.usecase.chatScreen

import com.ayub.khosa.chatapplication.domain.repository.ChatScreenRepository

class InsertMessageToFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {

    suspend operator fun invoke(
        chatRoomUUID: String,
        messageTitle: String,
        messageContent: String,
        registerUUID: String,
        reciver_fcmtoken: String
    ) = chatScreenRepository.insertMessageToFirebase(
        chatRoomUUID,
        messageTitle,
        messageContent,
        registerUUID,
        reciver_fcmtoken
    )
}