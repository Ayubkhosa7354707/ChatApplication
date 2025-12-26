package com.ayub.khosa.chatapplication.domain.usecase.homeScreen

import com.ayub.khosa.chatapplication.domain.repository.HomeRepository


class IsUserSignOutInFirebase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke() = homeRepository.isUserAuthenticatedInFirebase()
}