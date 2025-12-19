package com.ayub.khosa.chatapplication.repo

import android.content.Context
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

}