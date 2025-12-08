package com.ayub.khosa.chatapplication.repo

import android.content.SharedPreferences
import com.ayub.khosa.chatapplication.utils.Constant
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class MainActivityRepository @Inject constructor(
    private val fireMessage: FirebaseMessaging, private val pref: SharedPreferences
) {

    suspend fun getfcmtoken(): String {
        val token = fireMessage.token.await()

        return token
    }

    suspend fun sendMessage(message: HashMap<String, Any>): Boolean = try {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection(Constant.KEY_COLLECTION_CHAT).document("test_chat").set(message).await()
        true
    } catch (e: Exception) {
        false
    }


}