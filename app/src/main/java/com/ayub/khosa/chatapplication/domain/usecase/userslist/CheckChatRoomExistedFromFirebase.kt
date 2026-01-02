package com.ayub.khosa.chatapplication.domain.usecase.userslist

import com.ayub.khosa.chatapplication.domain.repository.ChatScreenRepository
import com.ayub.khosa.chatapplication.domain.repository.UserListScreenRepository
import com.ayub.khosa.chatapplication.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlin.String

class CheckChatRoomExistedFromFirebase
(private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(acceptorUUID: String) =
        userListScreenRepository.checkChatRoomExistedFromFirebase(acceptorUUID)
}