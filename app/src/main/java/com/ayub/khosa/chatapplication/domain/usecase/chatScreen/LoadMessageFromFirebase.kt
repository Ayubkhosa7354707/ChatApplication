package com.ayub.khosa.chatapplication.domain.usecase.chatScreen

import com.ayub.khosa.chatapplication.domain.repository.ChatScreenRepository

class LoadMessageFromFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(
        chatRoomUUID: String,
    ) = chatScreenRepository.loadMessageFromFirebase(
        chatRoomUUID,
    )
}