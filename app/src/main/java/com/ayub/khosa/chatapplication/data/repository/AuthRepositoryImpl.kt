package com.ayub.khosa.chatapplication.data.repository

import android.annotation.SuppressLint
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.ayub.khosa.chatapplication.domain.model.User
import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val fireMessage: FirebaseMessaging,
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser


    override suspend fun signIn(email: String, password: String): Flow<Response<Boolean>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                if (firebaseAuth.currentUser != null) {

//                    getfcmtoken()
                    this@callbackFlow.trySendBlocking(Response.Success(true))
                } else {
                    this@callbackFlow.trySendBlocking(Response.Success(false))
                }

            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
            }

            awaitClose {
                channel.close()
                cancel()
            }
        }

    override suspend fun onSignInWithGoogle(credential: Credential): Flow<Response<Boolean>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val idToken = googleIdTokenCredential.idToken

                    PrintLogs.printInfo(" googleIdToken :" + idToken)


                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                    firebaseAuth.signInWithCredential(firebaseCredential).await()
                    if (firebaseAuth.currentUser != null) {

                        this@callbackFlow.trySendBlocking(Response.Success(true))
                    } else {
                        this@callbackFlow.trySendBlocking(Response.Success(false))
                    }
                } else {
                    this@callbackFlow.trySendBlocking(Response.Success(false))
                }

            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
            }
            awaitClose {
                channel.close()
                cancel()
            }
        }


    override suspend fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>> = callbackFlow {

        try {
            this@callbackFlow.trySendBlocking(Response.Loading)
            if (firebaseAuth.currentUser != null) {

                this@callbackFlow.trySendBlocking(Response.Success(true))
            } else {
                this@callbackFlow.trySendBlocking(Response.Success(false))
            }
        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
        }
        awaitClose {
            channel.close()
            cancel()
        }

    }


    override suspend fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Response<Boolean>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)
                if (firebaseAuth.currentUser != null) {
                    val userUUID = firebaseAuth.currentUser?.uid.toString()
                    val displayName = firebaseAuth.currentUser?.displayName.toString()
                    val email = firebaseAuth.currentUser?.email.toString()
                    val image = ""+firebaseAuth.currentUser?.photoUrl.toString()
                    val fcmtoken = fireMessage.token.await()

                    val user = User(userUUID, email, displayName, fcmtoken ,image)
                    var databaseReference =
                        firebaseDatabase.getReference("Profiles").child(userUUID).child("profile")
                    databaseReference.setValue(user).await()

                    databaseReference =
                        firebaseDatabase.getReference("Profiles").child(userUUID).child("profile")
                            .child("status")
                    databaseReference.setValue(userStatus.toString()).await()
                    this@callbackFlow.trySendBlocking(Response.Success(true))
                } else {
                    this@callbackFlow.trySendBlocking(Response.Success(false))
                }

            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
            }
            awaitClose {
                channel.close()
                cancel()
            }
        }

    override fun signUp(
        email: String,
        password: String
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            this@callbackFlow.trySendBlocking(Response.Loading)

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                if (it.user != null) {
                    this@callbackFlow.trySendBlocking(Response.Success(true))
                }
            }.addOnFailureListener {
                this@callbackFlow.trySendBlocking(Response.Error("Error -> " + it.message))
            }
        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error("Error -> " + e.message))
        }
        awaitClose {
            channel.close()
            cancel()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    override suspend fun getfcmtoken(): Flow<Response<String>> = callbackFlow {

        try {
            this@callbackFlow.trySendBlocking(Response.Loading)

            val token = fireMessage.token.await()
            this@callbackFlow.trySendBlocking(Response.Success(token))


        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
        }
        awaitClose {
            channel.close()
            cancel()
        }


    }
}