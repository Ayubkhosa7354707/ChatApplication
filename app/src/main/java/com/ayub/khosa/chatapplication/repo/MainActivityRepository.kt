package com.ayub.khosa.chatapplication.repo

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class MainActivityRepository @Inject constructor(
    private val fireMessage: FirebaseMessaging,
    @ApplicationContext private val context: Context
) {

    suspend fun getfcmtoken(): String {
        val token = fireMessage.token.await()
        return token
    }

    suspend fun signInWithGoogle(idToken: String): AuthResult? {

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
       return  Firebase.auth.signInWithCredential(firebaseCredential).await()
    }

}