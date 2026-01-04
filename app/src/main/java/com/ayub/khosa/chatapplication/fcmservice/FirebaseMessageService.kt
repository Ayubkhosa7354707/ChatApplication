package com.ayub.khosa.chatapplication.fcmservice

import com.ayub.khosa.chatapplication.R
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ayub.khosa.chatapplication.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageService : FirebaseMessagingService() {


    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        PrintLogs.printInfo("Refreshed token: " + newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        PrintLogs.printInfo("RemoteMessage  title -->  "+remoteMessage.notification?.title)
        PrintLogs.printInfo("RemoteMessage  body -->  "+remoteMessage.notification?.body)

        PrintLogs.printInfo(" messageId " + remoteMessage.messageId)

        PrintLogs.printInfo("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            PrintLogs.printInfo("Message data payload: ${remoteMessage.data}")




        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val requestCode = 1

        val channelId = "Firebase Messaging ID"
        val channelName = "Firebase Messaging"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            )
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntentFlag = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) 0 else PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this, requestCode, intent, pendingIntentFlag)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
             .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(notificationId, notification)
    }


}