package com.ayub.khosa.chatapplication.domain.repository

import com.ayub.khosa.chatapplication.domain.model.User
import com.ayub.khosa.chatapplication.utils.Response
import kotlinx.coroutines.flow.Flow

interface UserListScreenRepository {
    suspend fun searchUserFromFirebase(userEmail: String): Flow<Response<User>>
    suspend fun createChatRoomToFirebase(reciver_UUID: String): Flow<Response<String>>
    suspend fun loadFriendListFromFirebase(): Flow<Response<List<User>>>

    suspend fun checkChatRoomExistedFromFirebase(reciver_UUID: String): Flow<Response<String>>
}