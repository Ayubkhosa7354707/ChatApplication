package com.ayub.khosa.chatapplication.data.repository

import com.ayub.khosa.chatapplication.domain.repository.AuthRepository
import com.ayub.khosa.chatapplication.domain.repository.HomeRepository
import com.ayub.khosa.chatapplication.utils.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : HomeRepository {

    override suspend fun signOut(): Flow<Response<Boolean>> = callbackFlow{
        try {
            this@callbackFlow.trySendBlocking(Response.Loading)
            firebaseAuth.signOut().apply {
                this@callbackFlow.trySendBlocking(Response.Success(true))
            }
        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error("Error ->"+e.message ))
        }

        awaitClose {
            channel.close()
            cancel()
        }

    }

    override  fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>> = callbackFlow {

        try {
            this@callbackFlow.trySendBlocking(Response.Loading)
            if (firebaseAuth.currentUser != null) {
                this@callbackFlow.trySendBlocking(Response.Success(true))
            } else {
                this@callbackFlow.trySendBlocking(Response.Success(false))
            }
        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error("Error ->"+e.message ))
        }


    }


}