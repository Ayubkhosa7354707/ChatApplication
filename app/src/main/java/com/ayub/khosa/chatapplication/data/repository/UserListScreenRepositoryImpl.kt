package com.ayub.khosa.chatapplication.data.repository

import com.ayub.khosa.chatapplication.domain.model.User
import com.ayub.khosa.chatapplication.domain.repository.UserListScreenRepository
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserListScreenRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
) : UserListScreenRepository {
    override suspend fun searchUserFromFirebase(userEmail: String): Flow<Response<User>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)

                val databaseReference = firebaseDatabase.getReference("Profiles")

                var user: User?

                databaseReference.get().addOnSuccessListener {
                    var flagForControl = false

                    val myJob = launch {
                        for (i in it.children) {
                            user = i.child("profile").getValue(User::class.java)!!
                            if (user?.userEmail == userEmail) {
                                flagForControl = true
                                PrintLogs.printInfo("<<-----searchUserFromFirebase ----->>  ")
                                PrintLogs.printInfo("searchUserFromFirebase -> email " + user.userEmail)
                                PrintLogs.printInfo("searchUserFromFirebase -> id " + user.profileUUID)
                                PrintLogs.printInfo("searchUserFromFirebase -> name " + user.userName)
                                PrintLogs.printInfo("searchUserFromFirebase -> fcmToken "+user.fcmToken)
                                this@callbackFlow.trySendBlocking(Response.Success(user))
                            }
                        }
                    }

                    myJob.invokeOnCompletion {
                        if (!flagForControl) {
                             PrintLogs.printE("searchUserFromFirebase -> User Not Found")
                            this@callbackFlow.trySendBlocking(Response.Error("User Not Found"))
                        }
                    }
                }.addOnFailureListener {
                    this@callbackFlow.trySendBlocking(Response.Error("Error -> " + it.message))
                }
                awaitClose {
                    channel.close()
                    cancel()
                }

            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error("Error -> " + e.message))
            }
        }

    override suspend fun createChatRoomToFirebase(acceptorUUID: String): Flow<Response<String>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)
                val requesterUUID = firebaseAuth.currentUser?.uid
                val hashMapOfRequesterUUIDAndAcceptorUUID = hashMapOf<String, String>()
                hashMapOfRequesterUUIDAndAcceptorUUID[requesterUUID!!] = acceptorUUID
                val databaseReference = firebaseDatabase.getReference("Chat_Rooms")
                val gson = Gson()
                val requesterUUIDAndAcceptorUUID =
                    gson.toJson(hashMapOfRequesterUUIDAndAcceptorUUID)
                databaseReference
                    .child(requesterUUIDAndAcceptorUUID)
                    .setValue(true)
                    .await()

                this@callbackFlow.trySendBlocking(Response.Success(requesterUUIDAndAcceptorUUID))
                awaitClose {
                    channel.close()
                    cancel()
                }
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error("Error -> " + e.message))
            }
        }

    override suspend fun loadFriendListFromFirebase(): Flow<Response<List<User>>> = callbackFlow {
        try {
            this@callbackFlow.trySendBlocking(Response.Loading)

            val databaseReference = firebaseDatabase.getReference("Profiles")

            var user: User?
            val friendList = mutableListOf<User>()
            databaseReference.get().addOnSuccessListener {



                    for (i in it.children) {
                        user = i.child("profile").getValue(User::class.java)!!

                        friendList.add(user)
                            PrintLogs.printInfo("<<-----loadFriendList ----->>  ")
                            PrintLogs.printInfo("loadFriendList -> email " + user.userEmail)
                            PrintLogs.printInfo("loadFriendList -> id " + user.profileUUID)
                            PrintLogs.printInfo("loadFriendList -> name " + user.userName)
                            PrintLogs.printInfo("loadFriendList -> fcmToken "+user.fcmToken)


                    }


                this@callbackFlow.trySendBlocking(Response.Success(friendList))
            }.addOnFailureListener {
                this@callbackFlow.trySendBlocking(Response.Error("Error -> " + it.message))
            }
            awaitClose {
                channel.close()
                cancel()
            }

        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error("Error -> " + e.message))
        }
    }

    override suspend fun checkChatRoomExistedFromFirebase(acceptorUUID: String): Flow<Response<String>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)

                val requesterUUID = firebaseAuth.currentUser?.uid

                val hashMapOfRequesterUUIDAndAcceptorUUID = hashMapOf<String, String>()
                hashMapOfRequesterUUIDAndAcceptorUUID[requesterUUID!!] = acceptorUUID

                val hashMapOfAcceptorUUIDAndRequesterUUID = hashMapOf<String, String>()
                hashMapOfAcceptorUUIDAndRequesterUUID[acceptorUUID] = requesterUUID

                val gson = Gson()
                val requesterUUIDAndAcceptorUUID =
                    gson.toJson(hashMapOfRequesterUUIDAndAcceptorUUID)
                val acceptorUUIDAndRequesterUUID =
                    gson.toJson(hashMapOfAcceptorUUIDAndRequesterUUID)

                val databaseReference = firebaseDatabase.getReference("Chat_Rooms")

                databaseReference.get().addOnSuccessListener {
                    try {
                        var keyListForControl = listOf<String>()
                        val hashMapForControl = hashMapOf<String, Any>()
                        for (i in it.children) {
                            val key = i.key as String
                            keyListForControl = keyListForControl + key
                            val hashMap: Map<String, Any> = Gson().fromJson(
                                i.key,
                                object : TypeToken<HashMap<String?, Any?>?>() {}.type
                            )

                            hashMapForControl.putAll(hashMap)
                        }

                        val chatRoomUUIDString: String?

                        if (keyListForControl.contains(requesterUUIDAndAcceptorUUID)) {

                            //ChatRoom opened by Requester
                            val hashMapOfRequesterUUIDAndAcceptorUUIDForSaveMessagesToFirebase =
                                hashMapOf<String, Any>()
                            hashMapOfRequesterUUIDAndAcceptorUUIDForSaveMessagesToFirebase[requesterUUID] =
                                acceptorUUID

                            val gson = Gson()
                            chatRoomUUIDString = gson.toJson(
                                hashMapOfRequesterUUIDAndAcceptorUUIDForSaveMessagesToFirebase
                            )

                            this@callbackFlow.trySendBlocking(Response.Success(chatRoomUUIDString!!))

                        } else if (keyListForControl.contains(acceptorUUIDAndRequesterUUID)) {

                            //ChatRoom opened by Acceptor
                            val hashMapOfAcceptorUUIDAndRequesterUUIDForSaveMessagesToFirebase =
                                hashMapOf<String, Any>()
                            hashMapOfAcceptorUUIDAndRequesterUUIDForSaveMessagesToFirebase[acceptorUUID] =
                                requesterUUID

                            val gson = Gson()
                            chatRoomUUIDString = gson.toJson(
                                hashMapOfAcceptorUUIDAndRequesterUUIDForSaveMessagesToFirebase
                            )

                            this@callbackFlow.trySendBlocking(Response.Success(chatRoomUUIDString!!))

                        } else {
                            this@callbackFlow.trySendBlocking(
                                Response.Success(
                                    "NO_CHATROOM_IN_FIREBASE_DATABASE"
                                )
                            )
                        }
                    } catch (e: JsonSyntaxException) {
                        this@callbackFlow.trySendBlocking(
                            Response.Error("Error -> " + e.message)
                        )
                    }
                }

                awaitClose {
                    channel.close()
                    cancel()
                }

            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error("Error -> " + e.message))
            }
        }

}