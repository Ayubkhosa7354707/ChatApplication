package com.ayub.khosa.chatapplication.domain.usecase.userslist

import com.ayub.khosa.chatapplication.domain.repository.UserListScreenRepository


class SearchUserFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(userEmail: String) =
        userListScreenRepository.searchUserFromFirebase(userEmail)
}