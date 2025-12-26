package com.ayub.khosa.chatapplication.domain.usecase.authScreen

import com.ayub.khosa.chatapplication.domain.repository.AuthRepository

class IsUserAuthenticatedInFirebase(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.isUserAuthenticatedInFirebase()
}