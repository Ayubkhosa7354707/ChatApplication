package com.ayub.khosa.chatapplication.domain.usecase.homeScreen

import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.domain.repository.HomeRepository


class SetUserStatusToFirebase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(userStatus: UserStatus) =
        homeRepository.setUserStatusToFirebase(userStatus)
}