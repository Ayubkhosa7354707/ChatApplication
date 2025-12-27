package com.ayub.khosa.chatapplication.domain.usecase.homeScreen

import com.ayub.khosa.chatapplication.domain.repository.HomeRepository


class IsUserAuthenticatedInFirebase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() = homeRepository.IsUserSignOutInFirebase()
}