package com.ayub.khosa.chatapplication.domain.usecase.homeScreen

import com.ayub.khosa.chatapplication.domain.repository.HomeRepository

class Getfcmtoken  (
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() =
        homeRepository.getfcmtoken()
}