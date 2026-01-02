package com.ayub.khosa.chatapplication.domain.usecase.authScreen

import androidx.credentials.Credential
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository

class OnSignInWithGoogle(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credential: Credential) =
        authRepository.onSignInWithGoogle(credential)
}