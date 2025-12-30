package com.ayub.khosa.chatapplication.data.repository

import com.ayub.khosa.chatapplication.domain.repository.UserListScreenRepository
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class UserListScreenRepositoryImpl  @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase,
) : UserListScreenRepository {
    override suspend fun searchUserFromFirebase(userEmail: String): Flow<Response<FirebaseUser>>  =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)

                val databaseReference = database.getReference("Profiles")

                var user: FirebaseUser?

                databaseReference.get().addOnSuccessListener {
                    var flagForControl = false

                    val myJob = launch {
                        for (i in it.children) {

                            PrintLogs.printInfo("searchUserFromFirebase 111 222 -> email "+i.child("profile").toString())
                            user = i.child("profile").getValue(FirebaseUser::class.java)!!


                            if (user?.email == userEmail) {
                                flagForControl = true
                                PrintLogs.printInfo("<<-----searchUserFromFirebase ----->>  ")
                                PrintLogs.printInfo("searchUserFromFirebase -> email "+user.email)

                                PrintLogs.printInfo("searchUserFromFirebase -> id "+user.uid)

                                PrintLogs.printInfo("searchUserFromFirebase -> name "+user.displayName)


                                this@callbackFlow.trySendBlocking(Response.Success(user))
                            }
                        }
                    }

                    myJob.invokeOnCompletion {
                        if (!flagForControl) {
                          // this@callbackFlow.trySendBlocking(Response.Success("empty"))
                            PrintLogs.printE("searchUserFromFirebase -> User Not Found")
                            this@callbackFlow.trySendBlocking(Response.Error("User Not Found"))
                        }
                    }

                }.addOnFailureListener {
                    this@callbackFlow.trySendBlocking(Response.Error("Error -> "+it.message ))
                }
                awaitClose {
                    channel.close()
                    cancel()
                }

            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error("Error -> "+e.message ))
            }
    }


}