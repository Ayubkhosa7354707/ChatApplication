package com.ayub.khosa.chatapplication.data.repository

import com.ayub.khosa.chatapplication.utils.Response
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

//    override suspend fun login(email: String, password: String): Response<FirebaseUser> {
//        return try {
//            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
//            Response.Success(result.user!!)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            PrintLogs.printE("login Error  "+e.message)
//            Response.Error(e.message ?: e.toString())
//        }
//    }



    override suspend fun signIn(email: String, password: String): Flow<Response<Boolean>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)
                firebaseAuth.signInWithEmailAndPassword(email, password).apply {
                    this.await()
                    if (this.isSuccessful) {
                        this@callbackFlow.trySendBlocking(Response.Success(true))
                    } else {
                        this@callbackFlow.trySendBlocking(Response.Success(false))
                    }
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




//    override suspend fun signOut(): Flow<Response<Boolean>> = callbackFlow{
//        return try {
//            firebaseAuth.signOut()
//            return Response.Success<Boolean> (true)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            PrintLogs.printE("logout Error  "+e.message)
//            return Response.Error(e.message ?: e.toString())
//        }
//    }


}