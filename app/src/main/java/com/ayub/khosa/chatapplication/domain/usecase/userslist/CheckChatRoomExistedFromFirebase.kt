package com.ayub.khosa.chatapplication.domain.usecase.userslist

import com.ayub.khosa.chatapplication.domain.repository.UserListScreenRepository

class CheckChatRoomExistedFromFirebase
    (
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(reciver_UUID: String) =
        userListScreenRepository.checkChatRoomExistedFromFirebase(reciver_UUID)
}