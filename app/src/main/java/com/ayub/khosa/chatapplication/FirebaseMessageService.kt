package com.ayub.khosa.chatapplication


import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseMessageService : FirebaseMessagingService() {


    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        PrintLogs.printInfo("Refreshed token: " + newToken)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        PrintLogs.printInfo(" messageId " + remoteMessage.messageId)

        PrintLogs.printInfo("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            PrintLogs.printInfo("Message data payload: ${remoteMessage.data}")


        }

        PrintLogs.printInfo(" message contains a notification payload   " + remoteMessage.notification?.title)

        PrintLogs.printInfo(" message contains a notification payload   " + remoteMessage.notification?.body)

        remoteMessage.notification?.let {
            PrintLogs.printInfo("Message Notification Body: ${it.body}")
        }


        sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)


    }

    private fun sendNotification(title: String?, body: String?) {

        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        val notification = NotificationCompat.Builder(this, Constant.KEY_notificationChannelID)
            .setContentTitle("Title :  " + title)
            .setContentText("Message : " + body)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification.build())

        PrintLogs.printInfo(" notification sent ")

    }
}






