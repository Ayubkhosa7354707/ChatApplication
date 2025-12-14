package com.ayub.khosa.chatapplication

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageService : FirebaseMessagingService() {


    companion object {
        private const val REPLY_ACTION_ID = "REPLY_ACTION_ID"
        private const val KEY_REPLY_TEXT = "KEY_REPLY_TEXT"


        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                return sharedPref?.getString(Constant.KEY_FCM_TOKEN, "")
            }
            set(value) {
                sharedPref?.edit()?.putString(Constant.KEY_FCM_TOKEN, value)?.apply()
            }
    }


    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        PrintLogs.printInfo("Refreshed token: " + newToken)
        token = newToken
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        PrintLogs.printInfo(" messageId " + remoteMessage.messageId)

        PrintLogs.printInfo("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            PrintLogs.printInfo("Message data payload: ${remoteMessage.data}")


        }

        PrintLogs.printInfo(" message contains a notification payload   title " + remoteMessage.notification?.title)

        PrintLogs.printInfo(" message contains a notification payload   " + remoteMessage.notification?.body)


    }

}




