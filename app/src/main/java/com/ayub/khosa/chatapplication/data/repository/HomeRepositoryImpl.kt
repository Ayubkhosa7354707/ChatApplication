package com.ayub.khosa.chatapplication.data.repository

import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository
import com.ayub.khosa.chatapplication.domain.repository.HomeRepository
import com.ayub.khosa.chatapplication.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth, private val database: FirebaseDatabase,
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

    override suspend fun IsUserSignOutInFirebase(): Flow<Response<Boolean>> = callbackFlow {

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
        awaitClose {
            channel.close()
            cancel()
        }

    }


    override suspend fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Response<Boolean>> = callbackFlow{
        try {
            this@callbackFlow.trySendBlocking(Response.Loading)
            if (firebaseAuth.currentUser != null) {
                val userUUID = firebaseAuth.currentUser?.uid.toString()
                val databaseReference =
                    database.getReference("Profiles").child(userUUID).child("profile")
                        .child("status")
                databaseReference.setValue(userStatus.toString()).await()
                this@callbackFlow.trySendBlocking(Response.Success(true))
            } else {
                this@callbackFlow.trySendBlocking(Response.Success(false))
            }

        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error("Error ->"+e.message ))
        }
        awaitClose {
            channel.close()
            cancel()
        }
    }


}