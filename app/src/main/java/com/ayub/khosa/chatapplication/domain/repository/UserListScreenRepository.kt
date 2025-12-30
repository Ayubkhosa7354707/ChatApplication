package com.ayub.khosa.chatapplication.domain.repository

import com.ayub.khosa.chatapplication.utils.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserListScreenRepository {
    suspend fun searchUserFromFirebase(userEmail: String): Flow<Response<FirebaseUser>>

}