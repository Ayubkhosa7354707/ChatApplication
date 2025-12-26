package com.ayub.khosa.chatapplication.repository

import com.ayub.khosa.chatapplication.model.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun logout(): Resource<Boolean>
}