package com.ayub.khosa.chatapplication.domain.usecase.authScreen

import com.ayub.khosa.chatapplication.domain.repository.AuthRepository

class SignUp(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signUp(email, password)
}