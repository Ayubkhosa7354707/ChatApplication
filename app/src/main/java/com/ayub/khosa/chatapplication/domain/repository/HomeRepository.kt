package com.ayub.khosa.chatapplication.domain.repository

import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.utils.Response
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun signOut(): Flow<Response<Boolean>>
    suspend fun IsUserSignOutInFirebase(): Flow<Response<Boolean>>
    suspend  fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Response<Boolean>>

}