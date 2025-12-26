package com.ayub.khosa.chatapplication.domain.repository

import com.ayub.khosa.chatapplication.utils.Response
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun signOut(): Flow<Response<Boolean>>
    fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>>
}