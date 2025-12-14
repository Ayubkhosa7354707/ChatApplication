package com.ayub.khosa.chatapplication.repo

import android.content.Context
import android.credentials.CredentialManager
import android.credentials.GetCredentialRequest
import com.ayub.khosa.chatapplication.network.Api
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class MainActivityRepository @Inject constructor(
    private val fireMessage: FirebaseMessaging,
    @ApplicationContext private val context: Context,
    private val fcmApi: Api
) {

    suspend fun getfcmtoken(): String {
        val token = fireMessage.token.await()

        return token
    }

    suspend fun sendMessage(messageBody: String) {
        PrintLogs.printInfo("MainActivityRepository sendMessage ")

      //  val credentialManager = CredentialManager.create(context)


        
        //  return fcmApi.sendMessage(messageBody = messageBody, header = remoteHeader)

    }


}