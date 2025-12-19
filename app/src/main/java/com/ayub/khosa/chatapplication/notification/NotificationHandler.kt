package com.ayub.khosa.chatapplication.notification



import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.ayub.khosa.chatapplication.R
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"

    // SIMPLE NOTIFICATION
    fun showSimpleNotification(title: String?, message: String?) {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("Title "+title)
            .setContentText("Message : " + message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }
}