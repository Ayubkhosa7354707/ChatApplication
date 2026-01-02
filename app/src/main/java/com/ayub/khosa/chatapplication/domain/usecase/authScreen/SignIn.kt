package com.ayub.khosa.chatapplication.domain.usecase.authScreen

import com.ayub.khosa.chatapplication.domain.repository.AuthRepository

class SignIn(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signIn(email, password)
}