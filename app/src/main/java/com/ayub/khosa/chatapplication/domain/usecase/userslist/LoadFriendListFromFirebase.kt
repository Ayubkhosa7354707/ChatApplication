package com.ayub.khosa.chatapplication.domain.usecase.userslist

import com.ayub.khosa.chatapplication.domain.repository.UserListScreenRepository

class LoadFriendListFromFirebase(
private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke() =
        userListScreenRepository.loadFriendListFromFirebase()
}