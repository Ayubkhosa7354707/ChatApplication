package com.ayub.khosa.chatapplication.domain.repository

import com.ayub.khosa.chatapplication.utils.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
//    suspend fun login(email: String, password: String): Response<FirebaseUser>
    suspend fun signIn(email: String, password: String): Flow<Response<Boolean>>

    fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>>

}