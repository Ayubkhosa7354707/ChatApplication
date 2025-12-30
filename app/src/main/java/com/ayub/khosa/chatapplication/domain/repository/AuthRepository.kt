package com.ayub.khosa.chatapplication.domain.repository


import androidx.credentials.Credential
import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.utils.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
//    suspend fun login(email: String, password: String): Response<FirebaseUser>
    suspend fun signIn(email: String, password: String): Flow<Response<Boolean>>

    suspend fun onSignInWithGoogle(credential: Credential ): Flow<Response<Boolean>>


    suspend fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>>
    suspend  fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Response<Boolean>>
    fun signUp(email: String, password: String):  Flow<Response<Boolean>>

}