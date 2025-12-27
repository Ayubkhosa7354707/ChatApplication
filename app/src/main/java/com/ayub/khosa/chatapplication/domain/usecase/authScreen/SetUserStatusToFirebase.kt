package com.ayub.khosa.chatapplication.domain.usecase.authScreen

import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository

class SetUserStatusToFirebase(
    private val authRepository: AuthRepository
) {
      suspend operator fun invoke(userStatus: UserStatus) =
        authRepository.setUserStatusToFirebase(userStatus)
}